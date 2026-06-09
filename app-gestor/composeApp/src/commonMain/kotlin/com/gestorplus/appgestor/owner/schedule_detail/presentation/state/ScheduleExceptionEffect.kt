package com.gestorplus.appgestor.owner.schedule_detail.presentation.state

sealed interface ScheduleExceptionEffect {
    data class ShowSnackbar(val message: String) : ScheduleExceptionEffect
    data object NavigateBack : ScheduleExceptionEffect
}