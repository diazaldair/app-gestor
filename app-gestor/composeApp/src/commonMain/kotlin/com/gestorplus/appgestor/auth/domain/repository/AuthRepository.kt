package com.gestorplus.appgestor.auth.domain.repository

import com.gestorplus.appgestor.auth.domain.model.UserRole
import com.gestorplus.appgestor.auth.domain.model.UserSession
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getActiveSession(): Flow<UserSession?>
    suspend fun loginWithEmail(email: String, password: String): Result<UserSession>
    suspend fun loginWithGoogle(idToken: String, selectedRole: UserRole): Result<UserSession>
    suspend fun registerDoctor(name: String, email: String, password: String): Result<UserSession>
    suspend fun registerPatient(name: String, email: String, password: String): Result<UserSession>
    suspend fun logout(): Result<Unit>
}
