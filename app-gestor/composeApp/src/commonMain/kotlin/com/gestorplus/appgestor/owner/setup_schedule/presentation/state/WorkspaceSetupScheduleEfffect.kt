package com.gestorplus.appgestor.owner.setup_schedule.presentation.state

sealed interface WorkspaceSetupScheduleEfffect {
    object NavigateToNextStep : WorkspaceSetupScheduleEfffect
    object NavigateBack : WorkspaceSetupScheduleEfffect
    data class ShowSnackbar(val message: String) : WorkspaceSetupScheduleEfffect
}
