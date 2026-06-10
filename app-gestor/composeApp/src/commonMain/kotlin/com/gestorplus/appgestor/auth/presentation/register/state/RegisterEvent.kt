package com.gestorplus.appgestor.auth.presentation.register.state

sealed interface RegisterEvent {
    data class FullNameChanged(val value: String) : RegisterEvent
    data class EmailChanged(val value: String) : RegisterEvent
    data class PasswordChanged(val value: String) : RegisterEvent
    data class ConfirmPasswordChanged(val value: String) : RegisterEvent
    data object TogglePasswordVisibility : RegisterEvent
    data class OnSubmitClicked(val role: String) : RegisterEvent
    data object OnGoogleRegisterClicked : RegisterEvent
    data object OnLoginClicked : RegisterEvent
}
