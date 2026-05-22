package com.gestorplus.appgestor.owner.setup_service.data.datasource.datasource

import com.gestorplus.appgestor.owner.setup_service.data.datasource.service.SetupServiceService

class SetupServiceRemoteDatasource(private val setupServiceService: SetupServiceService) {
    suspend fun saveWorkspaceService(uid: String, data: String) {
        setupServiceService.saveWorkspaceService(uid, data)
    }
}
