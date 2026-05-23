package com.gestorplus.appgestor.owner.setup_service.data.repository

import com.gestorplus.appgestor.owner.setup_service.data.datasource.SetupServiceRemoteDatasource
import com.gestorplus.appgestor.owner.setup_service.domain.model.WorkspaceService
import com.gestorplus.appgestor.owner.setup_service.domain.repository.SetupServiceRepository

import kotlinx.serialization.json.Json
import com.gestorplus.appgestor.data.datasource.FirebaseManager

class SetupServiceRepositoryImpl(
    private val remoteDatasource: SetupServiceRemoteDatasource,
    private val firebaseManager: FirebaseManager
) : SetupServiceRepository {

    override suspend fun saveWorkspaceService(service: WorkspaceService): Result<Unit> {
        return try {
            val dataString = Json.encodeToString(service)
            val currentUid = firebaseManager.getCurrentUserUid() ?: throw Exception("Usuario no autenticado")
            remoteDatasource.saveWorkspaceService(currentUid, dataString)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
