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

class SetupProfileRepositoryImpl(
    private val remoteDatasource: SetupProfileRemoteDatasource,
    private val firebaseManager: FirebaseManager,
    private val localPreferences: LocalPreferences,
    private val clinicProfileDao: ClinicProfileDao // Inyectamos el DAO
) : SetupProfileRepository {

    override suspend fun saveWorkspaceProfile(profile: WorkspaceProfile): Result<Unit> {
        return try {
            val currentUid = firebaseManager.getCurrentUserUid() 
                ?: throw Exception("Usuario no autenticado")

            // 1. Guardar en SharedPreferences (para compatibilidad con código existente)
            localPreferences.putString("registered_clinic_name", profile.clinicName)
            localPreferences.putString("registered_professional_name", profile.fullName)

            // 2. Persistencia en ROOM (Offline-First)
            val clinicEntity = ClinicProfileEntity(
                name = profile.fullName,
                bio = profile.biography,
                specialtiesJson = Json.encodeToString(profile.specialities),
                address = profile.exactAddress,
                mapUrl = null // Se actualizará después si es necesario
            )
            clinicProfileDao.insertProfile(clinicEntity)

            // 3. Subida de imágenes a Firebase
            val uploadedImages = profile.galleryImages.map { path ->
                if (path.startsWith("http")) path else firebaseManager.uploadImage(path)
            }
            
            val updatedProfile = profile.copy(galleryImages = uploadedImages)
            val dataString = Json.encodeToString(updatedProfile)

            // 4. Guardar en Firebase
            remoteDatasource.saveWorkspaceProfile(currentUid, dataString)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
