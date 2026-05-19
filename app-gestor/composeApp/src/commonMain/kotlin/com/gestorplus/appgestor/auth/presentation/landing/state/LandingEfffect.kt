package com.gestorplus.appgestor.auth.presentation.landing.state

sealed interface LandingEfffect {
    data object NavigateToPatientFlow : LandingEfffect
    data object NavigateToProfessionalFlow : LandingEfffect
}
