package com.gestorplus.appgestor.owner.domain.usecase

import com.gestorplus.appgestor.owner.domain.repository.OwnerRepository

class InitializeAndSyncConfigUseCase(private val repository: OwnerRepository) {
    suspend operator fun invoke(defaults: Map<String, String>) {
        repository.initializeAndSyncConfig(defaults)
    }
}
