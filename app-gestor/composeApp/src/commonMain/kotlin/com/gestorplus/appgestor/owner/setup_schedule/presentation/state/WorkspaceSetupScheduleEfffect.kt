package com.gestorplus.appgestor.owner.setup_schedule.presentation.state

sealed interface WorkspaceSetupScheduleEfffect {
    object NavigateToServices : WorkspaceSetupScheduleEfffect
    object NavigateBack : WorkspaceSetupScheduleEfffect
    data class ShowSnackbar(val message: String) : WorkspaceSetupScheduleEfffect
}
