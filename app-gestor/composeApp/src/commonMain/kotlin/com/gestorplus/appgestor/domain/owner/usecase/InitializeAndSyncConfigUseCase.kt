package com.gestorplus.appgestor.domain.owner.usecase

import com.gestorplus.appgestor.domain.owner.repository.OwnerRepository

class InitializeAndSyncConfigUseCase(private val repository: OwnerRepository) {
    suspend operator fun invoke(defaults: Map<String, String>) {
        repository.initializeAndSyncConfig(defaults)
    }
}
