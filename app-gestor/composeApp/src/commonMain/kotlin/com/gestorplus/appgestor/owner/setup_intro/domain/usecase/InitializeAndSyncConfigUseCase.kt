package com.gestorplus.appgestor.owner.setup_intro.domain.usecase

import com.gestorplus.appgestor.owner.setup_intro.domain.repository.SetupIntroRepository

class InitializeAndSyncConfigUseCase(private val repository: SetupIntroRepository) {
    suspend operator fun invoke(defaults: Map<String, String>) {
        repository.initializeAndSyncConfig(defaults)
    }
}
