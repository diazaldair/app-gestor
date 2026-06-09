package com.gestorplus.appgestor.services.data.repository

import com.gestorplus.appgestor.services.data.local.dao.ServiceDao
import com.gestorplus.appgestor.services.data.datasource.mapper.ServiceMapper
import com.gestorplus.appgestor.services.domain.model.ServiceModel
import com.gestorplus.appgestor.services.domain.repository.ServiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ServiceRepositoryImpl(
    private val serviceDao: ServiceDao,
    private val mapper: ServiceMapper
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
            serviceDao.insertService(mapper.toEntity(service))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleService(serviceId: String, isActive: Boolean) {
        serviceDao.updateServiceStatus(serviceId, isActive)
    }

    override suspend fun deleteService(serviceId: String) {
        serviceDao.deleteService(serviceId)
    }
}
