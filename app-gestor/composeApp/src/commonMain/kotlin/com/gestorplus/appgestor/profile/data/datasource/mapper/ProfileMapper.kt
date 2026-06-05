package com.gestorplus.appgestor.profile.data.datasource.mapper

import com.gestorplus.appgestor.core.data.local.entity.UserProfileEntity
import com.gestorplus.appgestor.profile.domain.model.UserProfile

class ProfileMapper {

    fun toDomain(entity: UserProfileEntity): UserProfile {
        return UserProfile(
            name = entity.name,
            email = entity.email,
            phone = entity.phone,
            description = entity.description,
            imageUrl = entity.imageUrl
        )
    }

    fun toEntity(domain: UserProfile): UserProfileEntity {
        return UserProfileEntity(
            name = domain.name,
            email = domain.email,
            phone = domain.phone,
            description = domain.description,
            imageUrl = domain.imageUrl
        )
    }
}
