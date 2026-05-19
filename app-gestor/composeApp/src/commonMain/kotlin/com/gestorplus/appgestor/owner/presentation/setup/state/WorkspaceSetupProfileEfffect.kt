package com.gestorplus.appgestor.owner.presentation.setup.state

sealed interface WorkspaceSetupProfileEfffect {
    data object NavigateToServices : WorkspaceSetupProfileEfffect
    data object NavigateBack : WorkspaceSetupProfileEfffect
    data class ShowSnackbar(val message: String) : WorkspaceSetupProfileEfffect
}
