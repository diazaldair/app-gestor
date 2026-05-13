package com.gestorplus.appgestor.services.domain.usecase

import com.gestorplus.appgestor.services.domain.repository.ServiceRepository

class GetServicesUseCase(private val repository: ServiceRepository) {
    operator fun invoke() = repository.getServices()
}
