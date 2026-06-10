package com.gestorplus.appgestor.services.data.repository

import com.gestorplus.appgestor.services.data.local.dao.ServiceDao
import com.gestorplus.appgestor.services.data.datasource.mapper.ServiceMapper
import com.gestorplus.appgestor.services.domain.model.ServiceModel
import com.gestorplus.appgestor.services.domain.repository.ServiceRepository
import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ServiceRepositoryImpl(
    private val serviceDao: ServiceDao,
    private val mapper: ServiceMapper,
    private val firebaseManager: FirebaseManager
) : ServiceRepository {

    override fun getServices(): Flow<List<ServiceModel>> {
        return serviceDao.getAllServices().map { entities ->
            entities.map { mapper.toDomain(it) }
        }
    }

    override suspend fun getServiceById(id: String): ServiceModel? {
        return serviceDao.getServiceById(id)?.let { mapper.toDomain(it) }
    }

    override suspend fun saveService(service: ServiceModel): Result<Unit> {
        return try {
            val uid = firebaseManager.getCurrentUserUid() ?: "anonymous"
            val entity = mapper.toEntity(service)
            
            // 1. Guardar Local
            serviceDao.insertService(entity)
            
            // 2. Sincronizar con Firebase en el nodo del consultorio
            val serviceData = mapOf(
                "name" to service.name,
                "description" to service.description,
                "price" to service.price,
                "durationMinutes" to service.durationMinutes,
                "category" to service.category,
                "currency" to service.currency,
                "isActive" to service.isActive
            )
            firebaseManager.saveData("workspaces/$uid/services/${entity.id}", serviceData)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleService(serviceId: String, isActive: Boolean) {
        serviceDao.updateServiceStatus(serviceId, isActive)
        val uid = firebaseManager.getCurrentUserUid() ?: return
        firebaseManager.saveData("workspaces/$uid/services/$serviceId/isActive", isActive)
    }

    override suspend fun deleteService(serviceId: String) {
        serviceDao.deleteService(serviceId)
        val uid = firebaseManager.getCurrentUserUid() ?: return
        // En Firebase para borrar usualmente se pone null o un nodo específico
        // firebaseManager.saveData("workspaces/$uid/services/$serviceId", null) // Necesitaría soporte en FirebaseManager
    }
}
