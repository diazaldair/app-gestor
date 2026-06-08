package com.gestorplus.appgestor.owner.setup_profile.data.datasource

interface SetupProfileRemoteDatasource {
    suspend fun saveWorkspaceProfile(uid: String, data: String)
}

class SetupProfileRemoteDatasourceImpl(private val setupProfileService: SetupProfileService) : SetupProfileRemoteDatasource {
    override suspend fun saveWorkspaceProfile(uid: String, data: String) {
        setupProfileService.saveWorkspaceProfile(uid, data)
    }
}
