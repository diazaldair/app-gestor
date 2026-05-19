package com.gestorplus.appgestor.auth.data.datasource.datasource

import com.gestorplus.appgestor.auth.data.datasource.dto.AuthUserDto
import com.gestorplus.appgestor.auth.data.datasource.service.AuthService

class AuthRemoteDatasource(private val authService: AuthService) {
    suspend fun login(email: String, password: String): AuthUserDto {
        val token = authService.loginWithEmail(email, password)
        // Extraer nombre para la simulación
        val simulatedName = email.substringBefore("@").replaceFirstChar { it.uppercase() }
        return AuthUserDto(
            id = "uid_${email.hashCode()}",
            email = email,
            name = simulatedName,
            role = "PROFESSIONAL",
            token = token
        )
    }
}
