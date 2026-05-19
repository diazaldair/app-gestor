package com.gestorplus.appgestor.owner.presentation.setup.state

sealed interface WorkspaceSetupIntroEvent {
    data object OnCreateWorkspaceClicked : WorkspaceSetupIntroEvent
}
