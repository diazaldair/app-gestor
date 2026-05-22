package com.gestorplus.appgestor.owner.setup_intro.presentation.state

sealed interface WorkspaceSetupIntroEvent {
    data object OnCreateWorkspaceClicked : WorkspaceSetupIntroEvent
}
