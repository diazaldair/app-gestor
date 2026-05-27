package com.gestorplus.appgestor.onboarding.domain.usecase

import com.gestorplus.appgestor.onboarding.domain.repository.OnboardingRepository

class CompleteOnboardingUseCase(private val repository: OnboardingRepository) {
    operator fun invoke() {
        repository.completeOnboarding()
    }
}
