package com.gestorplus.appgestor.onboarding.domain.usecase

import com.gestorplus.appgestor.onboarding.domain.repository.OnboardingRepository

class IsOnboardingCompletedUseCase(private val repository: OnboardingRepository) {
    operator fun invoke(): Boolean {
        return repository.isOnboardingCompleted()
    }
}
