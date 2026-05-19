package com.gestorplus.appgestor.onboarding.presentation.state

sealed interface LandingEvent {
    data object OnPatientSelected : LandingEvent
    data object OnProfessionalSelected : LandingEvent
}
