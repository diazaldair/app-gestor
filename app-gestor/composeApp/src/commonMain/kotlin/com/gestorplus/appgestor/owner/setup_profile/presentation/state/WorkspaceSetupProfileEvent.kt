package com.gestorplus.appgestor.owner.setup_profile.presentation.state

sealed interface WorkspaceSetupProfileEvent {
    data class ClinicNameChanged(val value: String) : WorkspaceSetupProfileEvent
    data class FullNameChanged(val value: String) : WorkspaceSetupProfileEvent
    data class InputSpecialityChanged(val value: String) : WorkspaceSetupProfileEvent
    data object AddSpecialityClicked : WorkspaceSetupProfileEvent
    data class RemoveSpecialityClicked(val speciality: String) : WorkspaceSetupProfileEvent
    data class BiographyChanged(val value: String) : WorkspaceSetupProfileEvent
    data object FixLocationClicked : WorkspaceSetupProfileEvent
    data class ExactAddressChanged(val value: String) : WorkspaceSetupProfileEvent
    data class ReferencesChanged(val value: String) : WorkspaceSetupProfileEvent
    data object AddPhotoClicked : WorkspaceSetupProfileEvent
    data class RemovePhotoClicked(val image: String) : WorkspaceSetupProfileEvent
    data object OnContinueClicked : WorkspaceSetupProfileEvent
    data object OnBackClicked : WorkspaceSetupProfileEvent
}
