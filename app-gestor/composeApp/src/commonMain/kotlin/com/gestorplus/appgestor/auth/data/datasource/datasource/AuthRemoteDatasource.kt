package com.gestorplus.appgestor.auth.data.datasource.datasource

import com.gestorplus.appgestor.auth.data.datasource.dto.AuthUserDto
import com.gestorplus.appgestor.auth.data.datasource.service.AuthService
import com.gestorplus.appgestor.auth.domain.model.UserRole

class AuthRemoteDatasource(private val authService: AuthService) {
    suspend fun login(email: String, password: String): AuthUserDto {
        val uid = authService.loginWithEmail(email, password)
        // Por ahora simulamos la obtención del nombre y rol. 
        // En una fase posterior, esto vendrá de la base de datos tras verificar ambos nodos.
        val simulatedName = email.substringBefore("@").replaceFirstChar { it.uppercase() }
        
        return AuthUserDto(
            id = uid,
            email = email,
            name = simulatedName,
            role = "PROFESSIONAL", // TODO: Implementar detección real de rol consultando Realtime Database
            token = uid
        )
    }

    suspend fun register(name: String, email: String, password: String, role: UserRole): AuthUserDto {
        val uid = authService.registerUser(name, email, password, role)
        return AuthUserDto(
            id = uid,
            email = email,
            name = name,
            role = role.name,
            token = uid
        )
    }
}
