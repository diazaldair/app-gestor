package com.gestorplus.appgestor.owner.setup_service.presentation.state

sealed interface WorkspaceSetupServiceEfffect {
    object NavigateToNextStep : WorkspaceSetupServiceEfffect // e.g. Finish setup
    object NavigateBack : WorkspaceSetupServiceEfffect
    data class ShowSnackbar(val message: String) : WorkspaceSetupServiceEfffect
}
