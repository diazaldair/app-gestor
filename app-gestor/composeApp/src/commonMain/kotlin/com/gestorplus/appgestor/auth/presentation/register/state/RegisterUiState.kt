package com.gestorplus.appgestor.auth.presentation.register.state

import androidx.compose.runtime.Immutable

@Immutable
data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
