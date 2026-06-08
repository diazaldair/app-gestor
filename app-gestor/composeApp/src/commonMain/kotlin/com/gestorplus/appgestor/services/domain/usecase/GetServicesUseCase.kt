package com.gestorplus.appgestor.services.domain.usecase

import com.gestorplus.appgestor.services.domain.model.ServiceModel
import com.gestorplus.appgestor.services.domain.repository.ServiceRepository
import kotlinx.coroutines.flow.Flow

class GetServicesUseCase(private val repository: ServiceRepository) {
    operator fun invoke(): Flow<List<ServiceModel>> = repository.getServices()
}
