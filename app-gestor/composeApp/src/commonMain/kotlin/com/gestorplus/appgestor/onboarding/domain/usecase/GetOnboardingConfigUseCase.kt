package com.gestorplus.appgestor.onboarding.domain.usecase

import com.gestorplus.appgestor.onboarding.domain.model.OnboardingSlide
import com.gestorplus.appgestor.onboarding.domain.repository.OnboardingRepository

class GetOnboardingConfigUseCase(private val repository: OnboardingRepository) {
    suspend operator fun invoke(): List<OnboardingSlide> {
        return repository.getSlides()
    }
}
