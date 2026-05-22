package com.gestorplus.appgestor.owner.setup_schedule.presentation.state

sealed interface WorkspaceSetupScheduleEvent {
    data class DayToggled(val day: String) : WorkspaceSetupScheduleEvent
    data class MorningStartChanged(val time: String) : WorkspaceSetupScheduleEvent
    data class MorningEndChanged(val time: String) : WorkspaceSetupScheduleEvent
    data class AfternoonStartChanged(val time: String) : WorkspaceSetupScheduleEvent
    data class AfternoonEndChanged(val time: String) : WorkspaceSetupScheduleEvent
    object OnContinueClicked : WorkspaceSetupScheduleEvent
    object OnBackClicked : WorkspaceSetupScheduleEvent
}
