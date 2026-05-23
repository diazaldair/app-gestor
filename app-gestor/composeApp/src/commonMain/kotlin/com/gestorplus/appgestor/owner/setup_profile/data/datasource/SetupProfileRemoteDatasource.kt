package com.gestorplus.appgestor.owner.setup_profile.data.datasource

class SetupProfileRemoteDatasource(private val setupProfileService: SetupProfileService) {
    suspend fun saveWorkspaceProfile(uid: String, data: String) {
        setupProfileService.saveWorkspaceProfile(uid, data)
    }
}
