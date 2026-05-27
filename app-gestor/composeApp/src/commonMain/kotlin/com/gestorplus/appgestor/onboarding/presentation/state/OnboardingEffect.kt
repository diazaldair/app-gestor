package com.gestorplus.appgestor.onboarding.presentation.state

sealed interface OnboardingEffect {
    data object NavigateToHome : OnboardingEffect
}
