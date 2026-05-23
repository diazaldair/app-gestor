package com.gestorplus.appgestor.auth.data.datasource.datasource

import com.gestorplus.appgestor.auth.data.datasource.dto.AuthUserDto
import com.gestorplus.appgestor.auth.data.datasource.service.AuthService

class AuthRemoteDatasource(private val authService: AuthService) {
    suspend fun login(email: String, password: String): AuthUserDto {
        val uid = authService.loginWithEmail(email, password)
        // Intentar obtener el nombre real desde la base de datos
        // En una app real esto podría ir en AuthService o aquí
        val simulatedName = email.substringBefore("@").replaceFirstChar { it.uppercase() }
        return AuthUserDto(
            id = uid,
            email = email,
            name = simulatedName, // TODO: Obtener de FirebaseDatabase usando getData("doctors/$uid/name")
            role = "PROFESSIONAL",
            token = uid
        )
    }

    suspend fun registerDoctor(name: String, email: String, password: String): AuthUserDto {
        val uid = authService.registerDoctor(name, email, password)
        return AuthUserDto(
            id = uid,
            email = email,
            name = name,
            role = "PROFESSIONAL",
            token = uid // En una app real aquí iría un JWT o session token, por ahora usamos el uid
        )
    }
}
