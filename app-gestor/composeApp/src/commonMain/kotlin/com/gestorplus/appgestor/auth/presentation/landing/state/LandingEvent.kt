package com.gestorplus.appgestor.auth.presentation.landing.state

sealed interface LandingEvent {
    data object OnPatientSelected : LandingEvent
    data object OnProfessionalSelected : LandingEvent
}
