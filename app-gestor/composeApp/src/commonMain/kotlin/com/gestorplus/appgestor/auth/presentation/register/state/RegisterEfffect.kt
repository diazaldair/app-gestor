package com.gestorplus.appgestor.auth.presentation.register.state

sealed interface RegisterEfffect {
    data object NavigateToHome : RegisterEfffect
    data object NavigateToLogin : RegisterEfffect
    data class ShowSnackbar(val message: String) : RegisterEfffect
}
