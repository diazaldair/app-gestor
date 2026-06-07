package com.gestorplus.appgestor.services.data.datasource.mapper

import com.gestorplus.appgestor.services.data.local.entity.ServiceEntity
import com.gestorplus.appgestor.services.domain.model.ServiceModel

class ServiceMapper {
    fun toDomain(entity: ServiceEntity): ServiceModel {
        return ServiceModel(
            id = entity.id,
            name = entity.name,
            category = entity.category,
            description = entity.description,
            price = entity.price,
            currency = entity.currency,
            durationMinutes = entity.durationMinutes,
            isActive = entity.isActive,
            imageUrl = entity.imageUrl
        )
    }

    fun toEntity(domain: ServiceModel): ServiceEntity {
        return ServiceEntity(
            id = domain.id,
            name = domain.name,
            category = domain.category,
            description = domain.description,
            price = domain.price,
            currency = domain.currency,
            durationMinutes = domain.durationMinutes,
            isActive = domain.isActive,
            imageUrl = domain.imageUrl
        )
    }
}
