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
    private val serviceDao: ServiceDao // Integración con el Dao de servicios
) : SetupServiceRepository {

    override suspend fun saveWorkspaceService(service: WorkspaceService): Result<Unit> {
        return try {
            val currentUid = firebaseManager.getCurrentUserUid() ?: "anonymous"
            
            // 1. Guardar en Remoto
            val dataString = Json.encodeToString(service)
            remoteDatasource.saveWorkspaceService(currentUid, dataString)

            // 2. Guardar en Local para que aparezca en el Catálogo inmediatamente
            val serviceEntity = ServiceEntity(
                id = "8", // Usamos el ID 8 solicitado para pruebas o uno dinámico
                name = service.serviceName,
                category = service.category,
                description = service.description,
                price = service.price.toDoubleOrNull() ?: 0.0,
                currency = "Bs",
                durationMinutes = 60,
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
