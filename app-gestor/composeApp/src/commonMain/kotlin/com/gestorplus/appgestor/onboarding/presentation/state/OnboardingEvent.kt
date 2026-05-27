package com.gestorplus.appgestor.onboarding.presentation.state

sealed interface OnboardingEvent {
    data object OnNextClicked : OnboardingEvent
    data object OnPreviousClicked : OnboardingEvent
    data object OnSkipClicked : OnboardingEvent
    data object OnStartClicked : OnboardingEvent
}
