package com.gestorplus.appgestor.auth.data.datasource.service

import com.gestorplus.appgestor.core.data.datasource.FirebaseManager

class AuthService(private val firebaseManager: FirebaseManager) {
    suspend fun loginWithEmail(email: String, password: String): String {
        return firebaseManager.loginUserWithEmail(email, password)
    }

    suspend fun registerUser(name: String, email: String, password: String, role: String): String {
        val uid = firebaseManager.registerUserWithEmail(email, password)
        
        val path = if (role == "PROFESSIONAL") "doctors/$uid" else "patients/$uid"
        firebaseManager.saveData("$path/name", name)
        firebaseManager.saveData("$path/email", email)
        firebaseManager.saveData("$path/role", role)
        firebaseManager.saveData("$path/registeredAt", System.currentTimeMillis().toString())
        
        return uid
    }
    
    suspend fun getUserRole(uid: String): String {
        // Primero buscamos en doctors
        val doctorData = firebaseManager.getData("doctors/$uid")
        if (doctorData != null) return "PROFESSIONAL"
        
        // Si no está, asumimos PATIENT o buscamos en patients
        return "PATIENT"
    }
}
