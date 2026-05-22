package com.gestorplus.appgestor.owner.setup_service.data.datasource.repository

import com.gestorplus.appgestor.owner.setup_service.data.datasource.datasource.SetupServiceRemoteDatasource
import com.gestorplus.appgestor.owner.setup_service.domain.model.WorkspaceService
import com.gestorplus.appgestor.owner.setup_service.domain.repository.SetupServiceRepository

class SetupServiceRepositoryImpl(
    private val remoteDatasource: SetupServiceRemoteDatasource
) : SetupServiceRepository {

    override suspend fun saveWorkspaceService(service: WorkspaceService): Result<Unit> {
        return try {
            // Serialización sencilla para mantener la coherencia con el resto del proyecto
            val dataString = """
                {
                    "name": "${service.name}",
                    "description": "${service.description}",
                    "price": ${service.price},
                    "currency": "${service.currency}",
                    "durationMinutes": ${service.durationMinutes}
                }
            """.trimIndent()
            
            val currentUid = "current_user_123" // TO-DO: obtener de Auth
            remoteDatasource.saveWorkspaceService(currentUid, dataString)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
