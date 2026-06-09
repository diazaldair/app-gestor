package com.gestorplus.appgestor.auth.data.datasource.service

import com.gestorplus.appgestor.auth.domain.model.AuthenticationProvider
import com.gestorplus.appgestor.auth.domain.model.FirebaseAuthenticatedUser
import com.gestorplus.appgestor.auth.domain.model.UserRole
import com.gestorplus.appgestor.data.datasource.FirebaseManager

class AuthService(private val firebaseManager: FirebaseManager) {
    suspend fun loginWithEmail(email: String, password: String): String {
        return firebaseManager.loginUserWithEmail(email, password)
    }

    suspend fun registerUser(name: String, email: String, password: String, role: UserRole): String {
        val uid = firebaseManager.registerUserWithEmail(email, password)
        
        val userPath = "${role.firebasePath}/$uid"
        val profileData = mapOf(
            "id" to uid,
            "name" to name,
            "email" to email,
            "role" to role.name,
            "registeredAt" to System.currentTimeMillis().toString(),
            "authProvider" to AuthenticationProvider.EMAIL_PASSWORD.name
        )
        firebaseManager.saveObject(userPath, profileData)
        
        return uid
    }

    suspend fun authenticateWithGoogle(idToken: String): FirebaseAuthenticatedUser {
        return firebaseManager.signInWithGoogleIdToken(idToken)
    }

    /**
     * Creates a social profile in a single atomic operation to avoid partial data states.
     */
    suspend fun createSocialProfile(uid: String, name: String, email: String, role: UserRole) {
        val userPath = "${role.firebasePath}/$uid"
        val profileData = mapOf(
            "id" to uid,
            "name" to name,
            "email" to email,
            "role" to role.name,
            "registeredAt" to System.currentTimeMillis().toString(),
            "authProvider" to AuthenticationProvider.GOOGLE.name
        )
        firebaseManager.saveObject(userPath, profileData)
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
