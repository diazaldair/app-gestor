package com.gestorplus.appgestor.clinicProfile.presentation.state

sealed interface ClinicProfileEfffect {
    data class ShowToast(val message: String) : ClinicProfileEfffect
    data object NavigateBack : ClinicProfileEfffect
}
