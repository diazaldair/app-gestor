package com.gestorplus.appgestor.onboarding.domain.repository

import com.gestorplus.appgestor.onboarding.domain.model.OnboardingSlide

interface OnboardingRepository {
    suspend fun getSlides(): List<OnboardingSlide>
    fun isOnboardingCompleted(): Boolean
    fun completeOnboarding()
}
