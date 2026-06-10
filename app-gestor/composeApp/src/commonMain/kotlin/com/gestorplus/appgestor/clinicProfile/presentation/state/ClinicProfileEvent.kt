package com.gestorplus.appgestor.clinicProfile.presentation.state

sealed interface ClinicProfileEvent {
    data class NameChanged(val name: String) : ClinicProfileEvent
    data class BiographyChanged(val bio: String) : ClinicProfileEvent
    data class AddressChanged(val address: String) : ClinicProfileEvent
    data class LocationUrlChanged(val url: String) : ClinicProfileEvent
    data class SpecialityInputChanged(val value: String) : ClinicProfileEvent
    data object AddSpecialityClicked : ClinicProfileEvent
    data class RemoveSpecialityClicked(val speciality: String) : ClinicProfileEvent
    data object SaveClicked : ClinicProfileEvent
    data object BackClicked : ClinicProfileEvent
}
