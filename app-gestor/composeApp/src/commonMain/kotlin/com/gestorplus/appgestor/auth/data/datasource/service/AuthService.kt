package com.gestorplus.appgestor.auth.data.datasource.service

import com.gestorplus.appgestor.data.datasource.FirebaseManager

class AuthService(private val firebaseManager: FirebaseManager) {
    suspend fun loginWithEmail(email: String, password: String): String {
        // En una app real, esto llama a Firebase Auth REST API o SDK.
        // Simulamos guardando los logs o un registro de sesión.
        val sessionPath = "auth/last_login"
        firebaseManager.saveData(sessionPath, "$email|${System.currentTimeMillis()}")
        
        // Retornamos un token simulado
        return "token_simulado_${email.hashCode()}"
    }
}
