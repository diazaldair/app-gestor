package com.gestorplus.appgestor.auth.presentation.login.state

import com.gestorplus.appgestor.auth.domain.model.UserRole

sealed interface LoginEfffect {
    data class NavigateToHome(val role: UserRole) : LoginEfffect
    data object NavigateToRegister : LoginEfffect
    data class ShowSnackbar(val message: String) : LoginEfffect
}
