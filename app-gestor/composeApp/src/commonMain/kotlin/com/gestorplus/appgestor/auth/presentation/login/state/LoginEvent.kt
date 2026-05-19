package com.gestorplus.appgestor.auth.presentation.login.state

sealed interface LoginEvent {
    data class EmailChanged(val value: String) : LoginEvent
    data class PasswordChanged(val value: String) : LoginEvent
    data object TogglePasswordVisibility : LoginEvent
    data object OnForgotPasswordClicked : LoginEvent
    data object OnSubmitClicked : LoginEvent
    data object OnGoogleLoginClicked : LoginEvent
    data object OnAppleLoginClicked : LoginEvent
    data object OnCreateAccountClicked : LoginEvent
}
