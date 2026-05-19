package com.gestorplus.appgestor.datetimeselector.data.mapper

import com.gestorplus.appgestor.datetimeselector.data.datasource.dto.TimeSlotDto
import com.gestorplus.appgestor.datetimeselector.domain.model.TimePeriod
import com.gestorplus.appgestor.datetimeselector.domain.model.TimeSlot

class DateTimeSelectorMapper {
    fun toDomain(dto: TimeSlotDto): TimeSlot {
        return TimeSlot(
            id = dto.id ?: "",
            time = dto.time ?: "",
            isAvailable = dto.isAvailable ?: true,
            period = try {
                TimePeriod.valueOf(dto.period ?: "MORNING")
            } catch (e: Exception) {
                TimePeriod.MORNING
            }
        )
    }
}
