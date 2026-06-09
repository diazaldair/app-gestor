package com.gestorplus.appgestor.owner.schedule_detail.presentation.state

import com.gestorplus.appgestor.owner.schedule.domain.model.ScheduleException

sealed interface ScheduleExceptionEvent {
    data class DateSelected(val date: String) : ScheduleExceptionEvent
    data class IsOpenChanged(val isOpen: Boolean) : ScheduleExceptionEvent
    data class CustomMorningStartChanged(val value: String) : ScheduleExceptionEvent
    data class CustomMorningEndChanged(val value: String) : ScheduleExceptionEvent
    data class CustomAfternoonStartChanged(val value: String) : ScheduleExceptionEvent
    data class CustomAfternoonEndChanged(val value: String) : ScheduleExceptionEvent
    data class ReasonChanged(val reason: String) : ScheduleExceptionEvent
    data object QuickFullDay : ScheduleExceptionEvent
    data object QuickLateStart : ScheduleExceptionEvent
    data object QuickEarlyClose : ScheduleExceptionEvent
    data object SaveException : ScheduleExceptionEvent
    data object DeleteException : ScheduleExceptionEvent
    data object LoadInitialData : ScheduleExceptionEvent
}