package com.gestorplus.appgestor.auth.data.datasource.service

import com.gestorplus.appgestor.auth.domain.model.UserRole
import com.gestorplus.appgestor.data.datasource.FirebaseManager

class AuthService(private val firebaseManager: FirebaseManager) {
    suspend fun loginWithEmail(email: String, password: String): String {
        return firebaseManager.loginUserWithEmail(email, password)
    }

    suspend fun registerUser(name: String, email: String, password: String, role: UserRole): String {
        val uid = firebaseManager.registerUserWithEmail(email, password)
        
        val userPath = "${role.firebasePath}/$uid"
        firebaseManager.saveData("$userPath/id", uid)
        firebaseManager.saveData("$userPath/name", name)
        firebaseManager.saveData("$userPath/email", email)
        firebaseManager.saveData("$userPath/role", role.name)
        firebaseManager.saveData("$userPath/registeredAt", System.currentTimeMillis().toString())
        
        return uid
    }

    suspend fun getUserProfile(uid: String, role: UserRole): Map<String, Any>? {
        val path = "${role.firebasePath}/$uid"
        return firebaseManager.getData(path)
    }

    suspend fun updateLastLogin(uid: String, role: UserRole) {
        val sessionPath = "${role.firebasePath}/$uid/last_login"
        firebaseManager.saveData(sessionPath, System.currentTimeMillis().toString())
    }
}
