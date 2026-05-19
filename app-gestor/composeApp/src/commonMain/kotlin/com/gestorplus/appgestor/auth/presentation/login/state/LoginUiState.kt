package com.gestorplus.appgestor.auth.presentation.login.state

import androidx.compose.runtime.Immutable

@Immutable
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
