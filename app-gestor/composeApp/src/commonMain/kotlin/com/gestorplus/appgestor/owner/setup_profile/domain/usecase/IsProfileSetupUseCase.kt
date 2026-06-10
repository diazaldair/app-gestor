package com.gestorplus.appgestor.owner.setup_profile.domain.usecase

import com.gestorplus.appgestor.owner.setup_profile.domain.repository.SetupProfileRepository

class IsProfileSetupUseCase(
    private val repository: SetupProfileRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.isProfileSetup().getOrDefault(false)
    }
}
