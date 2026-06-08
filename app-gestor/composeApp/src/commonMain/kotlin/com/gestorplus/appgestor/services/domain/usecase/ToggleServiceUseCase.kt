package com.gestorplus.appgestor.services.domain.usecase

import com.gestorplus.appgestor.services.domain.repository.ServiceRepository

class ToggleServiceUseCase(private val repository: ServiceRepository) {
    suspend operator fun invoke(serviceId: String, isActive: Boolean) {
        repository.toggleService(serviceId, isActive)
    }
}
