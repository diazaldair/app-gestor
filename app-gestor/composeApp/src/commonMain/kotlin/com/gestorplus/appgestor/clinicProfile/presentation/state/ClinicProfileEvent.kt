package com.gestorplus.appgestor.clinicProfile.presentation.state

sealed interface ClinicProfileEvent {
    data class OnNameChanged(val name: String) : ClinicProfileEvent
    data class OnBiographyChanged(val biography: String) : ClinicProfileEvent
    data class OnAddSpecialty(val specialty: String) : ClinicProfileEvent
    data class OnRemoveSpecialty(val specialty: String) : ClinicProfileEvent
    data class OnAddressChanged(val address: String) : ClinicProfileEvent
    data object OnSaveClicked : ClinicProfileEvent
}
