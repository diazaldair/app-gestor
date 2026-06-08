package com.gestorplus.appgestor.owner.setup_profile.data.repository

import com.gestorplus.appgestor.owner.setup_profile.data.datasource.SetupProfileRemoteDatasource
import com.gestorplus.appgestor.owner.setup_profile.domain.model.WorkspaceProfile
import com.gestorplus.appgestor.owner.setup_profile.domain.repository.SetupProfileRepository

import kotlinx.serialization.json.Json
import com.gestorplus.appgestor.core.data.datasource.FirebaseManager

class SetupProfileRepositoryImpl(
    private val remoteDatasource: SetupProfileRemoteDatasource,
    private val firebaseManager: FirebaseManager
) : SetupProfileRepository {

    override suspend fun saveWorkspaceProfile(profile: WorkspaceProfile): Result<Unit> {
        return try {
            val uploadedImages = profile.galleryImages.map { path ->
                if (path.startsWith("http") || path.startsWith("https")) {
                    path
                } else {
                    firebaseManager.uploadImage(path)
                }
            }
            val updatedProfile = profile.copy(galleryImages = uploadedImages)

            val dataString = Json.encodeToString(updatedProfile)
            val currentUid = firebaseManager.getCurrentUserUid() ?: throw Exception("Usuario no autenticado")
            remoteDatasource.saveWorkspaceProfile(currentUid, dataString)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
