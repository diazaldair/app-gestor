package com.gestorplus.appgestor.auth.data.datasource.service

import com.gestorplus.appgestor.data.datasource.FirebaseManager

class AuthService(private val firebaseManager: FirebaseManager) {
    suspend fun loginWithEmail(email: String, password: String): String {
        val sessionPath = "auth/last_login"
        firebaseManager.saveData(sessionPath, "$email|${System.currentTimeMillis()}")
        return "token_simulado_${email.hashCode()}"
    }

    suspend fun registerDoctor(name: String, email: String, password: String): String {
        // Simulamos el registro guardando datos en Firebase DB
        val doctorPath = "doctors/uid_${email.hashCode()}"
        firebaseManager.saveData("$doctorPath/name", name)
        firebaseManager.saveData("$doctorPath/email", email)
        firebaseManager.saveData("$doctorPath/registeredAt", System.currentTimeMillis().toString())
        return "token_register_simulado_${email.hashCode()}"
    }
}
