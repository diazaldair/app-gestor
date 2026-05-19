package com.gestorplus.appgestor.clinicprofile.presentation.state

sealed interface ClinicProfileEfffect {
    data class NavigateToBooking(val serviceId: String) : ClinicProfileEfffect
    data object NavigateBack : ClinicProfileEfffect
}
