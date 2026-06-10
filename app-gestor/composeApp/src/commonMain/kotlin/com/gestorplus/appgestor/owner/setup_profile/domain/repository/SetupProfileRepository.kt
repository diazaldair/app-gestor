package com.gestorplus.appgestor.owner.setup_profile.domain.repository

import com.gestorplus.appgestor.owner.setup_profile.domain.model.WorkspaceProfile

interface SetupProfileRepository {
    suspend fun saveWorkspaceProfile(profile: WorkspaceProfile): Result<Unit>
    suspend fun isProfileSetup(): Result<Boolean>
}
