package com.gestorplus.appgestor.auth.data.datasource.datasource

import com.gestorplus.appgestor.auth.data.datasource.dto.AuthUserDto
import com.gestorplus.appgestor.auth.data.datasource.service.AuthService

class AuthRemoteDatasource(private val authService: AuthService) {
    suspend fun login(email: String, password: String): AuthUserDto {
        val uid = authService.loginWithEmail(email, password)
        val role = authService.getUserRole(uid)
        
        return AuthUserDto(
            id = uid,
            email = email,
            name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
            role = role,
            token = uid
        )
    }

    suspend fun registerUser(name: String, email: String, password: String, role: String): AuthUserDto {
        val uid = authService.registerUser(name, email, password, role)
        return AuthUserDto(
            id = uid,
            email = email,
            name = name,
            role = role,
            token = uid
        )
    }
}
