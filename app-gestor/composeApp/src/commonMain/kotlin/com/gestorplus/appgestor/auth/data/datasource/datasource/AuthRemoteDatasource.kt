package com.gestorplus.appgestor.auth.data.datasource.datasource

import com.gestorplus.appgestor.auth.data.datasource.dto.AuthUserDto
import com.gestorplus.appgestor.auth.data.datasource.service.AuthService

class AuthRemoteDatasource(private val authService: AuthService) {
    suspend fun login(email: String, password: String): AuthUserDto {
        val token = authService.loginWithEmail(email, password)
        val simulatedName = email.substringBefore("@").replaceFirstChar { it.uppercase() }
        return AuthUserDto(
            id = "uid_${email.hashCode()}",
            email = email,
            name = simulatedName,
            role = "PROFESSIONAL",
            token = token
        )
    }

    suspend fun registerDoctor(name: String, email: String, password: String): AuthUserDto {
        val token = authService.registerDoctor(name, email, password)
        return AuthUserDto(
            id = "uid_${email.hashCode()}",
            email = email,
            name = name,
            role = "PROFESSIONAL",
            token = token
        )
    }
}
