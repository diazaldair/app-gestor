package com.gestorplus.appgestor.auth.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.auth.domain.usecase.LoginWithEmailUseCase
import com.gestorplus.appgestor.auth.presentation.login.state.LoginEfffect
import com.gestorplus.appgestor.auth.presentation.login.state.LoginEvent
import com.gestorplus.appgestor.auth.presentation.login.state.LoginUiState
import com.gestorplus.appgestor.owner.setup_profile.domain.usecase.IsProfileSetupUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
    private val isProfileSetupUseCase: IsProfileSetupUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEfffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        viewModelScope.launch {
            when (event) {
                is LoginEvent.EmailChanged -> {
                    _state.update { it.copy(email = event.value, errorMessage = null) }
                }
                is LoginEvent.PasswordChanged -> {
                    _state.update { it.copy(password = event.value, errorMessage = null) }
                }
                LoginEvent.TogglePasswordVisibility -> {
                    _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
                }
                LoginEvent.OnForgotPasswordClicked -> {
                    _effect.emit(LoginEfffect.ShowSnackbar("Funcionalidad para recuperar contraseña próximamente."))
                }
                LoginEvent.OnSubmitClicked -> {
                    submitLogin()
                }
                LoginEvent.OnGoogleLoginClicked -> {
                    _effect.emit(LoginEfffect.ShowSnackbar("Login con Google en desarrollo."))
                }
                LoginEvent.OnAppleLoginClicked -> {
                    _effect.emit(LoginEfffect.ShowSnackbar("Login con Apple no disponible en esta versión."))
                }
                LoginEvent.OnCreateAccountClicked -> {
                    _effect.emit(LoginEfffect.NavigateToRegister)
                }
            }
        }
    }

    private suspend fun submitLogin() {
        val email = _state.value.email
        val password = _state.value.password

        _state.update { it.copy(isLoading = true) }
        val result = loginWithEmailUseCase(email, password)
        _state.update { it.copy(isLoading = false) }

        result.fold(
            onSuccess = {
                val isSetup = isProfileSetupUseCase()
                if (isSetup) {
                    _effect.emit(LoginEfffect.NavigateToProfessionalDashboard)
                } else {
                    _effect.emit(LoginEfffect.NavigateToHome)
                }
            },
            onFailure = { error ->
                _state.update { it.copy(errorMessage = error.message ?: "Credenciales inválidas.") }
            }
        )
    }
}
