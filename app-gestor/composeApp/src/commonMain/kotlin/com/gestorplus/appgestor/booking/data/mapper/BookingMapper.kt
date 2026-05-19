package com.gestorplus.appgestor.booking.data.mapper

import com.gestorplus.appgestor.booking.data.dto.FirebaseBookingDto
import com.gestorplus.appgestor.booking.domain.model.BookingSlot
import com.gestorplus.appgestor.booking.domain.model.SlotPeriod

class BookingMapper {
    
    fun parseSlot(value: String): FirebaseBookingDto {
        if (value.isBlank()) return FirebaseBookingDto()

        val parts = value.split("|")
        return FirebaseBookingDto(
            time = parts.getOrNull(0)?.takeIf { it.isNotBlank() } ?: "00:00",
            isAvailable = parts.getOrNull(1)?.toBoolean() ?: true,
            period = try {
                SlotPeriod.valueOf(parts.getOrNull(2)?.takeIf { it.isNotBlank() } ?: "MORNING")
            } catch (e: Exception) {
                SlotPeriod.MORNING
            }
        )
    }

    fun toDomain(id: String, dto: FirebaseBookingDto): BookingSlot {
        return BookingSlot(
            id = id,
            time = dto.time,
            isAvailable = dto.isAvailable,
            period = dto.period
        )
    }
}
