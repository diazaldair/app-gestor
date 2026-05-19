package com.gestorplus.appgestor.auth.domain.usecase

import com.gestorplus.appgestor.auth.domain.model.UserSession
import com.gestorplus.appgestor.auth.domain.repository.AuthRepository

class LoginWithEmailUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<UserSession> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("El correo y la contraseña no pueden estar vacíos."))
        }
        return authRepository.loginWithEmail(email, password)
    }
}
