package com.gestorplus.appgestor.auth.data.datasource.mapper

import com.gestorplus.appgestor.auth.data.datasource.dto.AuthUserDto
import com.gestorplus.appgestor.auth.domain.model.UserSession

class AuthMapper {
    fun toDomain(dto: AuthUserDto): UserSession {
        return UserSession(
            userId = dto.id,
            email = dto.email,
            displayName = dto.name,
            role = dto.role,
            token = dto.token
        )
    }

    fun toDto(domain: UserSession): AuthUserDto {
        return AuthUserDto(
            id = domain.userId,
            email = domain.email,
            name = domain.displayName,
            role = domain.role,
            token = domain.token
        )
    }
}
