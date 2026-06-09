package com.gestorplus.appgestor.owner.setup_profile.data.repository

import com.gestorplus.appgestor.owner.setup_profile.data.datasource.SetupProfileRemoteDatasource
import com.gestorplus.appgestor.owner.setup_profile.domain.model.WorkspaceProfile
import com.gestorplus.appgestor.owner.setup_profile.domain.repository.SetupProfileRepository
import com.gestorplus.appgestor.core.persistence.LocalPreferences
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import com.gestorplus.appgestor.core.data.datasource.FirebaseManager

class SetupProfileRepositoryImpl(
    private val remoteDatasource: SetupProfileRemoteDatasource,
    private val firebaseManager: FirebaseManager,
    private val localPreferences: LocalPreferences
) : SetupProfileRepository {

    override suspend fun saveWorkspaceProfile(profile: WorkspaceProfile): Result<Unit> {
        return try {
            val currentUid = firebaseManager.getCurrentUserUid() 
                ?: throw Exception("Usuario no autenticado")

            // Guardamos localmente para mostrar en el Dashboard y Pantalla de Éxito
            localPreferences.putString("registered_clinic_name", profile.clinicName)
            localPreferences.putString("registered_professional_name", profile.fullName)

            val uploadedImages = profile.galleryImages.map { path ->
                if (path.startsWith("http")) {
                    path
                } else {
                    firebaseManager.uploadImage(path)
                }
            }
            
            val updatedProfile = profile.copy(galleryImages = uploadedImages)
            val dataString = Json.encodeToString(updatedProfile)

            remoteDatasource.saveWorkspaceProfile(currentUid, dataString)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
