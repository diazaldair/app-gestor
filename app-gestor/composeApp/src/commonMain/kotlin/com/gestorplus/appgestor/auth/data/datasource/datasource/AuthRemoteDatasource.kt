package com.gestorplus.appgestor.auth.data.datasource.datasource

import com.gestorplus.appgestor.auth.data.datasource.dto.AuthUserDto
import com.gestorplus.appgestor.auth.data.datasource.service.AuthService
import com.gestorplus.appgestor.auth.domain.model.UserRole

class AuthRemoteDatasource(private val authService: AuthService) {
    suspend fun login(email: String, password: String): AuthUserDto {
        val uid = authService.loginWithEmail(email, password)
        
        // Intentar obtener el perfil del nodo de pacientes
        val patientProfile = authService.getUserProfile(uid, UserRole.PATIENT)
        if (patientProfile != null) {
            authService.updateLastLogin(uid, UserRole.PATIENT)
            return AuthUserDto(
                id = uid,
                email = email,
                name = patientProfile["name"] as? String ?: email.substringBefore("@"),
                role = UserRole.PATIENT.name,
                token = uid
            )
        }

        // Si no es paciente, intentar obtener el perfil del nodo de profesionales (doctores)
        val doctorProfile = authService.getUserProfile(uid, UserRole.PROFESSIONAL)
        if (doctorProfile != null) {
            authService.updateLastLogin(uid, UserRole.PROFESSIONAL)
            return AuthUserDto(
                id = uid,
                email = email,
                name = doctorProfile["name"] as? String ?: email.substringBefore("@"),
                role = UserRole.PROFESSIONAL.name,
                token = uid
            )
        }

        // Si no existe en ningún nodo, lanzamos error para evitar accesos inconsistentes
        throw Exception("No se encontró un perfil asociado a esta cuenta. Por favor, regístrate.")
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
