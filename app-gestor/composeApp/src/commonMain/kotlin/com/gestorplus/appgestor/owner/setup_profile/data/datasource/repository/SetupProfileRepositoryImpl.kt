package com.gestorplus.appgestor.owner.setup_profile.data.datasource.repository

import com.gestorplus.appgestor.owner.setup_profile.data.datasource.datasource.SetupProfileRemoteDatasource
import com.gestorplus.appgestor.owner.setup_profile.domain.model.WorkspaceProfile
import com.gestorplus.appgestor.owner.setup_profile.domain.repository.SetupProfileRepository

class SetupProfileRepositoryImpl(
    private val remoteDatasource: SetupProfileRemoteDatasource
) : SetupProfileRepository {

    override suspend fun saveWorkspaceProfile(profile: WorkspaceProfile): Result<Unit> {
        return try {
            val dataString = """
                {
                    "clinicName": "${profile.clinicName}",
                    "fullName": "${profile.fullName}",
                    "specialities": "${profile.specialities.joinToString(",")}",
                    "biography": "${profile.biography}",
                    "exactAddress": "${profile.exactAddress}",
                    "references": "${profile.references}",
                    "galleryImages": "${profile.galleryImages.joinToString(",")}"
                }
            """.trimIndent()
            val currentUid = "current_user_123" // TO-DO: get from Auth
            remoteDatasource.saveWorkspaceProfile(currentUid, dataString)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
