package com.gestorplus.appgestor.onboarding.presentation.state

sealed interface LandingEfffect {
    data object NavigateToPatientFlow : LandingEfffect
    data object NavigateToProfessionalFlow : LandingEfffect
}
