package com.gestorplus.appgestor.services.data.repository

import com.gestorplus.appgestor.services.domain.model.Service
import com.gestorplus.appgestor.services.domain.repository.ServiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ServiceRepositoryImpl : ServiceRepository {
    override fun getServices(): Flow<List<Service>> {
        return flowOf(
            listOf(
                Service("1", "Full Haircut & Beard", 65.0, 20.0, 30),
                Service("2", "Consultation Session", 120.0, 0.0),
                Service("3", "Quick Trim", 35.0, 10.0)
            )
        )
    }
}
