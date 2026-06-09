package com.gestorplus.appgestor.owner.setup_schedule.data.datasource.mapper

import com.gestorplus.appgestor.owner.setup_schedule.data.local.entity.ShiftEntity
import com.gestorplus.appgestor.owner.setup_schedule.domain.model.Shift
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class ShiftMapper {
    fun toEntity(domain: Shift): ShiftEntity {
        return ShiftEntity(
            id = domain.id,
            name = domain.name,
            startTime = domain.startTime,
            endTime = domain.endTime,
            daysJson = Json.encodeToString(domain.days)
        )
    }

    fun toDomain(entity: ShiftEntity): Shift {
        return Shift(
            id = entity.id,
            name = entity.name,
            startTime = entity.startTime,
            endTime = entity.endTime,
            days = Json.decodeFromString(entity.daysJson)
        )
    }
}
