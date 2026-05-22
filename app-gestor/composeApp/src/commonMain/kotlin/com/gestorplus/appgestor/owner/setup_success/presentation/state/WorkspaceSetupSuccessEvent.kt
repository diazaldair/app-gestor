package com.gestorplus.appgestor.owner.setup_success.presentation.state

sealed interface WorkspaceSetupSuccessEvent {
    object OnLoadData : WorkspaceSetupSuccessEvent
    object OnGoToConsoleClicked : WorkspaceSetupSuccessEvent
}
