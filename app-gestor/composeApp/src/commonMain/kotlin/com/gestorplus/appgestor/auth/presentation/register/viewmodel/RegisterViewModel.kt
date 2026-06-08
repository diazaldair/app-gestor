package com.gestorplus.appgestor.auth.presentation.register.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.auth.domain.model.UserRole
import com.gestorplus.appgestor.auth.domain.usecase.RegisterDoctorUseCase
import com.gestorplus.appgestor.auth.domain.usecase.RegisterPatientUseCase
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
    private val registerDoctorUseCase: RegisterDoctorUseCase,
    private val registerPatientUseCase: RegisterPatientUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<RegisterEfffect>()
    val effect = _effect.asSharedFlow()

    private var userRole: UserRole = UserRole.PROFESSIONAL

    fun setRole(role: UserRole) {
        userRole = role
    }

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
                RegisterEvent.OnSubmitClicked -> {
                    submitRegister()
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

    private suspend fun submitRegister() {
        val fullName = _state.value.fullName
        val email = _state.value.email
        val password = _state.value.password
        val confirmPassword = _state.value.confirmPassword

        if (fullName.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _state.update { it.copy(errorMessage = "Todos los campos son obligatorios.") }
            return
        }

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        if (!emailRegex.matches(email)) {
            _state.update { it.copy(errorMessage = "Formato de correo inválido.") }
            return
        }

        val passwordRegex = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$".toRegex()
        if (!passwordRegex.matches(password)) {
            _state.update { it.copy(errorMessage = "La contraseña debe tener 8 caracteres, 1 mayúscula y 1 número.") }
            return
        }

        if (password != confirmPassword) {
            _state.update { it.copy(errorMessage = "Las contraseñas no coinciden.") }
            return
        }

        _state.update { it.copy(isLoading = true) }
        
        val result = if (userRole == UserRole.PATIENT) {
            registerPatientUseCase(fullName, email, password, confirmPassword)
        } else {
            registerDoctorUseCase(fullName, email, password, confirmPassword)
        }

        _state.update { it.copy(isLoading = false) }

        result.fold(
            onSuccess = {
                _effect.emit(RegisterEfffect.NavigateToHome)
            },
            onFailure = { error ->
                _state.update { it.copy(errorMessage = error.message ?: "Error al registrar la cuenta.") }
            }
        )
    }
}
