package com.gestorplus.appgestor.auth.presentation.register.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.auth.domain.usecase.RegisterUserUseCase
import com.gestorplus.appgestor.auth.presentation.register.state.RegisterEfffect
import com.gestorplus.appgestor.auth.presentation.register.state.RegisterEvent
import com.gestorplus.appgestor.auth.presentation.register.state.RegisterUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<RegisterEfffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: RegisterEvent) {
        viewModelScope.launch {
            when (event) {
                is RegisterEvent.FullNameChanged -> {
                    _state.update { it.copy(fullName = event.value, errorMessage = null) }
                }
                is RegisterEvent.EmailChanged -> {
                    _state.update { it.copy(email = event.value, errorMessage = null) }
                }
                is RegisterEvent.PasswordChanged -> {
                    _state.update { it.copy(password = event.value, errorMessage = null) }
                }
                is RegisterEvent.ConfirmPasswordChanged -> {
                    _state.update { it.copy(confirmPassword = event.value, errorMessage = null) }
                }
                RegisterEvent.TogglePasswordVisibility -> {
                    _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
                }
                is RegisterEvent.OnSubmitClicked -> {
                    submitRegister(event.role)
                }
                RegisterEvent.OnGoogleRegisterClicked -> {
                    _effect.emit(RegisterEfffect.ShowSnackbar("Registro con Google próximamente."))
                }
                RegisterEvent.OnLoginClicked -> {
                    _effect.emit(RegisterEfffect.NavigateToLogin)
                }
            }
        }
    }

    private suspend fun submitRegister(role: String) {
        val fullName = _state.value.fullName
        val email = _state.value.email
        val password = _state.value.password
        val confirmPassword = _state.value.confirmPassword

        _state.update { it.copy(isLoading = true) }
        val result = registerUserUseCase(fullName, email, password, confirmPassword, role)
        _state.update { it.copy(isLoading = false) }

        result.fold(
            onSuccess = { session ->
                // Emitimos el efecto con el rol para que App.kt sepa a dónde navegar
                _effect.emit(RegisterEfffect.NavigateToHome)
            },
            onFailure = { error ->
                _state.update { it.copy(errorMessage = error.message ?: "Error al registrar la cuenta.") }
            }
        )
    }
}
