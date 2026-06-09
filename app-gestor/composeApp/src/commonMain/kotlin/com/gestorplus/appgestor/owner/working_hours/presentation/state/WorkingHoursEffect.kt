package com.gestorplus.appgestor.owner.working_hours.presentation.state

sealed interface WorkingHoursEffect {
    data class ShowSnackbar(val message: String) : WorkingHoursEffect
    data object NavigateBack : WorkingHoursEffect
    data class NavigateToExceptions(val date: String?) : WorkingHoursEffect
}