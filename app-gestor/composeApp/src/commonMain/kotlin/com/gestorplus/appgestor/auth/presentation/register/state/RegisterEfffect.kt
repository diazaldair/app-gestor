package com.gestorplus.appgestor.auth.presentation.register.state

import com.gestorplus.appgestor.auth.domain.model.UserRole

sealed interface RegisterEfffect {
    data class NavigateToHome(val role: UserRole) : RegisterEfffect
    data object NavigateToLogin : RegisterEfffect
    data class ShowSnackbar(val message: String) : RegisterEfffect
}
