package com.gestorplus.appgestor.auth.presentation.login.state

sealed interface LoginEfffect {
    data object NavigateToHome : LoginEfffect
    data object NavigateToProfessionalDashboard : LoginEfffect
    data object NavigateToRegister : LoginEfffect
    data class ShowSnackbar(val message: String) : LoginEfffect
}
