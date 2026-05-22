package com.gestorplus.appgestor.owner.setup_profile.presentation.state

sealed interface WorkspaceSetupProfileEfffect {
    data object NavigateToServices : WorkspaceSetupProfileEfffect
    data object NavigateBack : WorkspaceSetupProfileEfffect
    data class ShowSnackbar(val message: String) : WorkspaceSetupProfileEfffect
}
