package com.gestorplus.appgestor.owner.presentation.setup.state

sealed interface WorkspaceSetupIntroEfffect {
    data object NavigateToNextStep : WorkspaceSetupIntroEfffect
    data class ShowSnackbar(val message: String) : WorkspaceSetupIntroEfffect
}
