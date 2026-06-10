package com.gestorplus.appgestor.owner.setup_profile.data.repository

import com.gestorplus.appgestor.owner.setup_profile.data.datasource.SetupProfileRemoteDatasource
import com.gestorplus.appgestor.owner.setup_profile.domain.model.WorkspaceProfile
import com.gestorplus.appgestor.owner.setup_profile.domain.repository.SetupProfileRepository
import com.gestorplus.appgestor.core.persistence.LocalPreferences
import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import com.gestorplus.appgestor.clinicProfile.data.local.dao.ClinicProfileDao
import com.gestorplus.appgestor.clinicProfile.data.local.entity.ClinicProfileEntity
import com.gestorplus.appgestor.notification.domain.NotificationRepository
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
    private val clinicProfileDao: ClinicProfileDao,
    private val notificationRepository: NotificationRepository
) : SetupProfileRepository {

    override suspend fun saveWorkspaceProfile(profile: WorkspaceProfile): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val currentUid = firebaseManager.getCurrentUserUid() 
                ?: throw Exception("Sesión expirada. Por favor, inicia sesión de nuevo.")

            // 1. Guardar FCM Token para que el Dr reciba notificaciones push
            try {
                val token = notificationRepository.getFCMToken()
                if (token != null) {
                    firebaseManager.saveData("workspaces/$currentUid/fcmToken", token)
                    firebaseManager.saveData("users/$currentUid/fcmToken", token)
                }
            } catch (e: Exception) {
                println("DEBUG: Error al obtener FCM Token: ${e.message}")
            }

            // 2. Persistencia Local Rápida
            localPreferences.putString("registered_clinic_name", profile.clinicName)
            localPreferences.putString("registered_professional_name", profile.fullName)
            localPreferences.putBoolean("is_profile_setup", true)

            // 3. Persistencia en ROOM
            val clinicEntity = ClinicProfileEntity(
                name = profile.fullName,
                bio = profile.biography,
                specialtiesJson = Json.encodeToString(profile.specialities),
                address = profile.exactAddress,
                locationUrl = profile.locationUrl,
                latitude = profile.latitude,
                longitude = profile.longitude
            )
            clinicProfileDao.insertProfile(clinicEntity)

            // 4. Subida de imágenes
            val uploadedImages = supervisorScope {
                profile.galleryImages.mapIndexed { index, path ->
                    async {
                        if (path.startsWith("http")) path 
                        else firebaseManager.uploadImage(path)
                    }
                }.awaitAll()
            }

            // 5. Guardar Perfil Completo en Firebase
            val updatedProfile = profile.copy(galleryImages = uploadedImages)
            remoteDatasource.saveWorkspaceProfile(currentUid, Json.encodeToString(updatedProfile))

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isProfileSetup(): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val localSetup = localPreferences.getBoolean("is_profile_setup", false)
            if (localSetup) return@withContext Result.success(true)

            val currentUid = firebaseManager.getCurrentUserUid() ?: return@withContext Result.success(false)
            val data = firebaseManager.getData("workspaces/$currentUid/profile")
            
            val isSetup = data != null
            if (isSetup) localPreferences.putBoolean("is_profile_setup", true)
            
            Result.success(isSetup)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
