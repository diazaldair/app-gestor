package com.gestorplus.appgestor.auth.data.datasource.datasource

import com.gestorplus.appgestor.auth.data.datasource.dto.AuthUserDto
import com.gestorplus.appgestor.auth.data.datasource.service.AuthService
import com.gestorplus.appgestor.auth.domain.model.GoogleSignInFailure
import com.gestorplus.appgestor.auth.domain.model.UserRole

class AuthRemoteDatasource(private val authService: AuthService) {
    suspend fun login(email: String, password: String): AuthUserDto {
        val uid = authService.loginWithEmail(email, password)
        
        // Intentar obtener el perfil del nodo de pacientes
        val patientProfile = authService.getUserProfile(uid, UserRole.PATIENT)
        if (patientProfile != null) {
            safeUpdateLastLogin(uid, UserRole.PATIENT)
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
            safeUpdateLastLogin(uid, UserRole.PROFESSIONAL)
            return AuthUserDto(
                id = uid,
                email = email,
                name = doctorProfile["name"] as? String ?: email.substringBefore("@"),
                role = UserRole.PROFESSIONAL.name,
                token = uid
            )
        }

        // Si no existe en ningun nodo, lanzamos error para evitar accesos inconsistentes
        throw Exception("No se encontro un perfil asociado a esta cuenta. Por favor, registrate.")
    }

    suspend fun loginWithGoogle(idToken: String, selectedRole: UserRole): AuthUserDto {
        val firebaseUser = authService.authenticateWithGoogle(idToken)
        val uid = firebaseUser.uid
        val email = firebaseUser.email
        val displayName = firebaseUser.displayName

        if (email.isBlank()) throw GoogleSignInFailure.InvalidGoogleIdToken

        val patientProfile = authService.getUserProfile(uid, UserRole.PATIENT)
        val doctorProfile = authService.getUserProfile(uid, UserRole.PROFESSIONAL)

        if (patientProfile != null && doctorProfile != null) {
            throw GoogleSignInFailure.DuplicateRoleProfile
        }

        return when {
            patientProfile != null -> {
                safeUpdateLastLogin(uid, UserRole.PATIENT)
                AuthUserDto(
                    id = uid,
                    email = email,
                    name = patientProfile["name"] as? String ?: displayName.ifBlank { email.substringBefore("@") },
                    role = UserRole.PATIENT.name,
                    token = uid
                )
            }
            doctorProfile != null -> {
                safeUpdateLastLogin(uid, UserRole.PROFESSIONAL)
                AuthUserDto(
                    id = uid,
                    email = email,
                    name = doctorProfile["name"] as? String ?: displayName.ifBlank { email.substringBefore("@") },
                    role = UserRole.PROFESSIONAL.name,
                    token = uid
                )
            }
            firebaseUser.isNewUser -> {
                val finalName = displayName.ifBlank { email.substringBefore("@") }
                
                // 1. Intentar creacion atomica del perfil
                try {
                    authService.createSocialProfile(uid, finalName, email, selectedRole)
                } catch (e: Exception) {
                    // Si la creacion del perfil falla, el login no puede proceder
                    throw GoogleSignInFailure.ProfileCreationFailure
                }
                
                // 2. Intento de auditoria (no bloquea el acceso si falla)
                safeUpdateLastLogin(uid, selectedRole)
                
                AuthUserDto(
                    id = uid,
                    email = email,
                    name = finalName,
                    role = selectedRole.name,
                    token = uid
                )
            }
            else -> {
                // El usuario ya existe en Auth pero no tiene perfil en la BD
                throw GoogleSignInFailure.ProfileMissing
            }
        }
    }

    /**
     * Actualiza la fecha de ultimo acceso de forma segura.
     * Si falla, se ignora el error para permitir que el usuario continue con el login,
     * ya que la auditoria no es una operacion critica para la sesion.
     */
    private suspend fun safeUpdateLastLogin(uid: String, role: UserRole) {
        try {
            authService.updateLastLogin(uid, role)
        } catch (e: Exception) {
            // Error de auditoria silencioso
        }
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
