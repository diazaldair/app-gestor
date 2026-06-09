package com.gestorplus.appgestor.owner.setup_service.data.repository

import com.gestorplus.appgestor.owner.setup_service.data.datasource.SetupServiceRemoteDatasource
import com.gestorplus.appgestor.owner.setup_service.domain.model.WorkspaceService
import com.gestorplus.appgestor.owner.setup_service.domain.repository.SetupServiceRepository
import com.gestorplus.appgestor.services.data.local.dao.ServiceDao
import com.gestorplus.appgestor.services.data.local.entity.ServiceEntity
import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SetupServiceRepositoryImpl(
    private val remoteDatasource: SetupServiceRemoteDatasource,
    private val firebaseManager: FirebaseManager,
    private val serviceDao: ServiceDao
) : SetupServiceRepository {

    override suspend fun saveWorkspaceService(service: WorkspaceService): Result<Unit> {
        return try {
            val currentUid = firebaseManager.getCurrentUserUid() ?: "anonymous"
            
            val dataString = Json.encodeToString(service)
            remoteDatasource.saveWorkspaceService(currentUid, dataString)

            val serviceEntity = ServiceEntity(
                id = "8", 
                name = service.name,
                category = "General", // WorkspaceService no tiene categoría, asignamos una por defecto
                description = service.description,
                price = service.price, // Ya es Double, no necesita toDoubleOrNull
                currency = service.currency,
                durationMinutes = service.durationMinutes,
                isActive = true,
                imageUrl = null
            )
            serviceDao.insertService(serviceEntity)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
