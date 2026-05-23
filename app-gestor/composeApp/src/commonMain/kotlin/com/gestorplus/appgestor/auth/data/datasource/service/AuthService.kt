package com.gestorplus.appgestor.auth.data.datasource.service

import com.gestorplus.appgestor.data.datasource.FirebaseManager

class AuthService(private val firebaseManager: FirebaseManager) {
    suspend fun loginWithEmail(email: String, password: String): String {
        // Hacemos login en Firebase Auth y obtenemos el UID
        val uid = firebaseManager.loginUserWithEmail(email, password)
        
        // Opcional: Actualizar la última fecha de sesión en Realtime Database
        val sessionPath = "doctors/$uid/last_login"
        firebaseManager.saveData(sessionPath, System.currentTimeMillis().toString())
        
        return uid
    }

    suspend fun registerDoctor(name: String, email: String, password: String): String {
        // Registramos en Firebase Auth y obtenemos el UID
        val uid = firebaseManager.registerUserWithEmail(email, password)
        
        // Guardamos los metadatos en Firebase Realtime Database
        val doctorPath = "doctors/$uid"
        firebaseManager.saveData("$doctorPath/name", name)
        firebaseManager.saveData("$doctorPath/email", email)
        firebaseManager.saveData("$doctorPath/registeredAt", System.currentTimeMillis().toString())
        
        return uid
    }
}
