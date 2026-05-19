package com.gestorplus.appgestor.clinicprofile.presentation.state

sealed interface ClinicProfileEvent {
    data object OnBackClicked : ClinicProfileEvent
    data class OnReserveService(val serviceId: String) : ClinicProfileEvent
}
