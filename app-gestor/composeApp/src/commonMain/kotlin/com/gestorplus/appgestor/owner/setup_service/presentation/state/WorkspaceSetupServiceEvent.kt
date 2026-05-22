package com.gestorplus.appgestor.owner.setup_service.presentation.state

sealed interface WorkspaceSetupServiceEvent {
    data class ServiceNameChanged(val name: String) : WorkspaceSetupServiceEvent
    data class DescriptionChanged(val description: String) : WorkspaceSetupServiceEvent
    data class PriceChanged(val price: String) : WorkspaceSetupServiceEvent
    data class DurationOptionSelected(val option: DurationOption) : WorkspaceSetupServiceEvent
    object IncrementCustomHours : WorkspaceSetupServiceEvent
    object DecrementCustomHours : WorkspaceSetupServiceEvent
    object IncrementCustomMinutes : WorkspaceSetupServiceEvent
    object DecrementCustomMinutes : WorkspaceSetupServiceEvent
    object OnContinueClicked : WorkspaceSetupServiceEvent
    object OnBackClicked : WorkspaceSetupServiceEvent
}
