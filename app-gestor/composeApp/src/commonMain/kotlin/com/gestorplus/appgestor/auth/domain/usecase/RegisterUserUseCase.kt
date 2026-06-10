package com.gestorplus.appgestor.auth.domain.usecase

import com.gestorplus.appgestor.auth.domain.model.UserSession
import com.gestorplus.appgestor.auth.domain.repository.AuthRepository

class RegisterUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(name: String, email: String, password: String, confirmPassword: String, role: String): Result<UserSession> {
        if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            return Result.failure(IllegalArgumentException("Por favor, completa todos los campos."))
        }
        if (!email.contains("@") || !email.contains(".")) {
            return Result.failure(IllegalArgumentException("Por favor, ingresa un correo electrónico válido."))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("La contraseña debe tener al menos 6 caracteres."))
        }
        if (password != confirmPassword) {
            return Result.failure(IllegalArgumentException("Las contraseñas no coinciden."))
        }
        return authRepository.registerUser(name, email, password, role)
    }
}
