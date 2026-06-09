package com.gestorplus.appgestor.auth.domain.usecase

import com.gestorplus.appgestor.auth.domain.model.UserRole
import com.gestorplus.appgestor.auth.domain.model.UserSession
import com.gestorplus.appgestor.auth.domain.repository.AuthRepository

class LoginWithGoogleUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(idToken: String, selectedRole: UserRole): Result<UserSession> {
        return authRepository.loginWithGoogle(idToken, selectedRole)
    }
}
