package com.gestorplus.appgestor.auth.data.datasource.repository

import com.gestorplus.appgestor.auth.data.datasource.datasource.AuthLocalDatasource
import com.gestorplus.appgestor.auth.data.datasource.datasource.AuthRemoteDatasource
import com.gestorplus.appgestor.auth.data.datasource.mapper.AuthMapper
import com.gestorplus.appgestor.auth.domain.model.UserRole
import com.gestorplus.appgestor.auth.domain.model.UserSession
import com.gestorplus.appgestor.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val localDatasource: AuthLocalDatasource,
    private val remoteDatasource: AuthRemoteDatasource,
    private val mapper: AuthMapper
) : AuthRepository {

    override fun getActiveSession(): Flow<UserSession?> {
        return localDatasource.getSessionFlow().map { dto ->
            dto?.let { mapper.toDomain(it) }
        }
    }

    override suspend fun loginWithEmail(email: String, password: String): Result<UserSession> {
        return try {
            val userDto = remoteDatasource.login(email, password)
            localDatasource.saveSession(userDto)
            Result.success(mapper.toDomain(userDto))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerDoctor(name: String, email: String, password: String): Result<UserSession> {
        return try {
            val userDto = remoteDatasource.register(name, email, password, UserRole.PROFESSIONAL)
            localDatasource.saveSession(userDto)
            Result.success(mapper.toDomain(userDto))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerPatient(name: String, email: String, password: String): Result<UserSession> {
        return try {
            val userDto = remoteDatasource.register(name, email, password, UserRole.PATIENT)
            localDatasource.saveSession(userDto)
            Result.success(mapper.toDomain(userDto))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            localDatasource.clearSession()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
