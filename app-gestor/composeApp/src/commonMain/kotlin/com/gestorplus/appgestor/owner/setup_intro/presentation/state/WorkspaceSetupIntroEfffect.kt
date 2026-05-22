package com.gestorplus.appgestor.owner.setup_intro.presentation.state

sealed interface WorkspaceSetupIntroEfffect {
    data object NavigateToNextStep : WorkspaceSetupIntroEfffect
    data class ShowSnackbar(val message: String) : WorkspaceSetupIntroEfffect
}
