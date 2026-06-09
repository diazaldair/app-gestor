package com.gestorplus.appgestor.owner.working_hours.presentation.state

sealed interface WorkingHoursEfffect {
    data object NavigateBack : WorkingHoursEfffect
    data class ShowSnackbar(val message: String) : WorkingHoursEfffect
}
