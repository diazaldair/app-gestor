package com.gestorplus.appgestor.services.domain.repository

import com.gestorplus.appgestor.services.domain.model.ServiceModel
import kotlinx.coroutines.flow.Flow

interface ServiceRepository {
    fun getServices(): Flow<List<ServiceModel>>
    suspend fun getServiceById(id: String): ServiceModel?
    suspend fun saveService(service: ServiceModel): Result<Unit>
    suspend fun toggleService(serviceId: String, isActive: Boolean)
    suspend fun deleteService(serviceId: String)
}
