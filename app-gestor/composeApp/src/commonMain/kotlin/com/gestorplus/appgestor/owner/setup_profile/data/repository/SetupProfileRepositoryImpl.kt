package com.gestorplus.appgestor.owner.setup_profile.data.repository

import com.gestorplus.appgestor.owner.setup_profile.data.datasource.SetupProfileRemoteDatasource
import com.gestorplus.appgestor.owner.setup_profile.domain.model.WorkspaceProfile
import com.gestorplus.appgestor.owner.setup_profile.domain.repository.SetupProfileRepository
import com.gestorplus.appgestor.core.persistence.LocalPreferences
import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import com.gestorplus.appgestor.clinicProfile.data.local.dao.ClinicProfileDao
import com.gestorplus.appgestor.clinicProfile.data.local.entity.ClinicProfileEntity
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class SetupProfileRepositoryImpl(
    private val remoteDatasource: SetupProfileRemoteDatasource,
    private val firebaseManager: FirebaseManager,
    private val localPreferences: LocalPreferences,
    private val clinicProfileDao: ClinicProfileDao 
) : SetupProfileRepository {

    override suspend fun saveWorkspaceProfile(profile: WorkspaceProfile): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val currentUid = firebaseManager.getCurrentUserUid() 
                ?: throw Exception("Sesión expirada. Por favor, inicia sesión de nuevo.")

            println("DEBUG: [Repository] Iniciando guardado de perfil para UID: $currentUid")

            // 1. Persistencia Local Rápida
            try {
                localPreferences.putString("registered_clinic_name", profile.clinicName)
                localPreferences.putString("registered_professional_name", profile.fullName)
                localPreferences.putBoolean("is_profile_setup", true)
                println("DEBUG: [Repository] LocalPreferences guardado correctamente")
            } catch (e: Exception) {
                println("DEBUG: [Repository] Error en LocalPreferences: ${e.message}")
            }

            // 2. Persistencia en ROOM
            try {
                val clinicEntity = ClinicProfileEntity(
                    name = profile.fullName,
                    bio = profile.biography,
                    specialtiesJson = Json.encodeToString(profile.specialities),
                    address = profile.exactAddress,
                    mapUrl = profile.mapUrl,
                    latitude = profile.latitude,
                    longitude = profile.longitude
                )
                clinicProfileDao.insertProfile(clinicEntity)
                println("DEBUG: [Repository] Room: Perfil insertado correctamente")
            } catch (e: Exception) {
                println("DEBUG: [Repository] Error en Room (ignorable): ${e.message}")
            }

            // 3. Subida de imágenes
            println("DEBUG: [Repository] Subiendo ${profile.galleryImages.size} imágenes...")
            val uploadedImages = supervisorScope {
                profile.galleryImages.mapIndexed { index, path ->
                    async {
                        try {
                            if (path.startsWith("http")) {
                                println("DEBUG: [Repository] Imagen [$index] ya es URL: $path")
                                path 
                            } else {
                                println("DEBUG: [Repository] Imagen [$index] subiendo path local: $path")
                                val resultUrl = firebaseManager.uploadImage(path)
                                println("DEBUG: [Repository] Imagen [$index] subida con éxito: $resultUrl")
                                resultUrl
                            }
                        } catch (e: Exception) {
                            println("DEBUG: [Repository] ERROR subiendo imagen [$index] ($path): ${e.message}")
                            throw e
                        }
                    }
                }.awaitAll()
            }
            
            println("DEBUG: [Repository] Todas las imágenes subidas: $uploadedImages")

            // 4. Guardar Perfil Completo en Firebase
            val updatedProfile = profile.copy(galleryImages = uploadedImages)
            val dataString = Json.encodeToString(updatedProfile)
            
            try {
                remoteDatasource.saveWorkspaceProfile(currentUid, dataString)
                println("DEBUG: [Repository] Perfil guardado exitosamente en Firebase (Realtime DB)")
            } catch (e: Exception) {
                println("DEBUG: [Repository] Error guardando en Firebase (Realtime DB): ${e.message}")
                throw e
            }

            Result.success(Unit)
        } catch (e: Exception) {
            println("DEBUG: [Repository] ERROR FATAL al guardar perfil: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun isProfileSetup(): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            // 1. Check local preference first for speed
            val localSetup = localPreferences.getBoolean("is_profile_setup", false)
            if (localSetup) return@withContext Result.success(true)

            // 2. Check Firebase if not in local
            val currentUid = firebaseManager.getCurrentUserUid() ?: return@withContext Result.success(false)
            val data = firebaseManager.getData("workspaces/$currentUid/profile")
            
            val isSetup = data != null
            if (isSetup) {
                // Sync to local for next time
                localPreferences.putBoolean("is_profile_setup", true)
            }
            
            Result.success(isSetup)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
