package com.gestorplus.appgestor.services.domain.repository

import com.gestorplus.appgestor.services.domain.model.ServiceModel
import kotlinx.coroutines.flow.Flow

interface ServiceRepository {
    fun getServices(): Flow<List<ServiceModel>>
    suspend fun toggleService(serviceId: String, isActive: Boolean)
    suspend fun deleteService(serviceId: String)
}
