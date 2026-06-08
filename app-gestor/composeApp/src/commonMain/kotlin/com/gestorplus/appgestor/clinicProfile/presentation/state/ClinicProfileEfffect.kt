package com.gestorplus.appgestor.clinicProfile.presentation.state

sealed interface ClinicProfileEfffect {
    data object NavigateBack : ClinicProfileEfffect
    data class ShowMessage(val message: String) : ClinicProfileEfffect
}
