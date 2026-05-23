package com.gestorplus.appgestor.owner.setup_service.data.datasource

class SetupServiceRemoteDatasource(private val setupServiceService: SetupServiceService) {
    suspend fun saveWorkspaceService(uid: String, data: String) {
        setupServiceService.saveWorkspaceService(uid, data)
    }
}
