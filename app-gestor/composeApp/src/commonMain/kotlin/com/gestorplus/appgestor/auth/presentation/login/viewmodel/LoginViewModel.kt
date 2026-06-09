package com.gestorplus.appgestor.auth.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.auth.domain.model.GoogleSignInFailure
import com.gestorplus.appgestor.auth.domain.model.UserRole
import com.gestorplus.appgestor.auth.domain.service.GoogleSignInService
import com.gestorplus.appgestor.auth.domain.usecase.LoginWithEmailUseCase
import com.gestorplus.appgestor.auth.domain.usecase.LoginWithGoogleUseCase
import com.gestorplus.appgestor.auth.presentation.login.state.LoginEfffect
import com.gestorplus.appgestor.auth.presentation.login.state.LoginEvent
import com.gestorplus.appgestor.auth.presentation.login.state.LoginUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val googleSignInService: GoogleSignInService
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
                    _effect.emit(LoginEfffect.ShowSnackbar("Funcionalidad para recuperar contrasena proximamente."))
                }
                LoginEvent.OnSubmitClicked -> {
                    submitLogin()
                }
                is LoginEvent.OnGoogleLoginClicked -> {
                    signInWithGoogle(event.role)
                }
                LoginEvent.OnAppleLoginClicked -> {
                    _effect.emit(LoginEfffect.ShowSnackbar("Login con Apple no disponible en esta version."))
                }
                LoginEvent.OnCreateAccountClicked -> {
                    _effect.emit(LoginEfffect.NavigateToRegister)
                }
            }
        }
    }

    private suspend fun submitLogin() {
        if (_state.value.isLoading) return

        val email = _state.value.email
        val password = _state.value.password

        if (email.isBlank() || password.isBlank()) {
            _state.update { it.copy(errorMessage = "Por favor, completa todos los campos.") }
            return
        }

        _state.update { it.copy(isLoading = true, errorMessage = null) }
        
        try {
            val result = loginWithEmailUseCase(email, password)
            result.fold(
                onSuccess = { session ->
                    val role = UserRole.entries.find { it.name == session.role }
                    
                    if (role != null) {
                        _effect.emit(LoginEfffect.NavigateToHome(role))
                    } else {
                        _state.update { it.copy(errorMessage = "La cuenta tiene un rol invalido. Contacta con soporte.") }
                    }
                },
                onFailure = { error ->
                    _state.update { it.copy(errorMessage = error.message ?: "Credenciales invalidas.") }
                }
            )
        } catch (e: Exception) {
            _state.update { it.copy(errorMessage = "Ocurrio un error inesperado. Intenta nuevamente.") }
        } finally {
            _state.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun signInWithGoogle(selectedRole: UserRole) {
        if (_state.value.isLoading) return

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        try {
            val tokenResult = googleSignInService.requestGoogleIdToken()

            tokenResult.fold(
                onSuccess = { idToken ->
                    val loginResult = loginWithGoogleUseCase(idToken, selectedRole)
                    
                    loginResult.fold(
                        onSuccess = { session ->
                            val role = UserRole.entries.find { it.name == session.role }

                            if (role != null) {
                                _effect.emit(LoginEfffect.NavigateToHome(role))
                            } else {
                                _effect.emit(LoginEfffect.ShowSnackbar("La cuenta tiene un rol invalido. Contacta con soporte."))
                            }
                        },
                        onFailure = { error ->
                            val message = mapGoogleFailureToMessage(error)
                            _effect.emit(LoginEfffect.ShowSnackbar(message))
                        }
                    )
                },
                onFailure = { error ->
                    if (error is GoogleSignInFailure.UserCancelled) {
                        // No mostrar error si el usuario cancelo explicitamente
                    } else {
                        val message = mapGoogleFailureToMessage(error)
                        _effect.emit(LoginEfffect.ShowSnackbar(message))
                    }
                }
            )
        } catch (e: Exception) {
            _effect.emit(LoginEfffect.ShowSnackbar("Ocurrio un error inesperado. Intenta nuevamente."))
        } finally {
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun mapGoogleFailureToMessage(error: Throwable): String {
        return when (error) {
            is GoogleSignInFailure.UserCancelled -> "Inicio de sesion cancelado."
            is GoogleSignInFailure.NoCredentialAvailable -> "No se encontro una cuenta de Google disponible."
            is GoogleSignInFailure.InvalidCredentialType,
            is GoogleSignInFailure.InvalidGoogleIdToken,
            is GoogleSignInFailure.InvalidGoogleCredential -> "No se pudo validar la cuenta de Google."
            is GoogleSignInFailure.AccountExistsWithDifferentCredential -> "Ya existe una cuenta con este correo. Inicia sesion con tu metodo original."
            is GoogleSignInFailure.UserDisabled -> "Esta cuenta de usuario ha sido deshabilitada."
            is GoogleSignInFailure.TooManyRequests -> "Se realizaron demasiados intentos. Intenta nuevamente mas tarde."
            is GoogleSignInFailure.NetworkFailure -> "No se pudo conectar. Revisa tu conexion a Internet."
            is GoogleSignInFailure.ProfileMissing -> "Tu cuenta existe, pero no tiene un perfil configurado. Contacta con soporte."
            is GoogleSignInFailure.DuplicateRoleProfile -> "La cuenta presenta una inconsistencia de perfil. Contacta con soporte."
            is GoogleSignInFailure.ProfileCreationFailure -> "No se pudo completar la creacion del perfil."
            else -> "Ocurrio un error inesperado. Intenta nuevamente."
        }
    }
}
