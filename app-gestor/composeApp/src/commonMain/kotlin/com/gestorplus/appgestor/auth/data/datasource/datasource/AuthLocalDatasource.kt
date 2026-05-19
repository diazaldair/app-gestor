package com.gestorplus.appgestor.auth.data.datasource.datasource

import com.gestorplus.appgestor.auth.data.datasource.dto.AuthUserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthLocalDatasource {
    private val _cachedSession = MutableStateFlow<AuthUserDto?>(null)

    fun getSessionFlow(): Flow<AuthUserDto?> {
        return _cachedSession.asStateFlow()
    }

    suspend fun saveSession(dto: AuthUserDto) {
        _cachedSession.value = dto
    }

    suspend fun clearSession() {
        _cachedSession.value = null
    }
}
