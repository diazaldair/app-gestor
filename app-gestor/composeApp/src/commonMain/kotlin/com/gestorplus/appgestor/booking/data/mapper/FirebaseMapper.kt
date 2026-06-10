package com.gestorplus.appgestor.booking.data.mapper

import com.gestorplus.appgestor.booking.data.datasource.dto.FirebaseBookingDto
import com.gestorplus.appgestor.booking.domain.model.SlotPeriod

class FirebaseMapper {
    fun parseBooking(value: String): FirebaseBookingDto {
        if (value.isBlank()) return FirebaseBookingDto()
        val parts = value.split("|")
        return FirebaseBookingDto(
            clientName = parts.getOrNull(0)?.takeIf { it.isNotBlank() } ?: "Unknown",
            serviceName = parts.getOrNull(1)?.takeIf { it.isNotBlank() } ?: "General Service",
            status = parts.getOrNull(2)?.takeIf { it.isNotBlank() } ?: "PENDING"
        )
    }

    fun parseSlot(value: String): FirebaseBookingDto {
        if (value.isBlank()) return FirebaseBookingDto()
        val parts = value.split("|")
        return FirebaseBookingDto(
            time = parts.getOrNull(0)?.takeIf { it.isNotBlank() } ?: "00:00",
            // Fallback a true si no es explícitamente "false"
            isAvailable = parts.getOrNull(1)?.let { it.lowercase() != "false" } ?: true,
            period = try {
                SlotPeriod.valueOf(parts.getOrNull(2)?.takeIf { it.isNotBlank() } ?: "MORNING")
            } catch (e: Exception) {
                SlotPeriod.MORNING
            }
        )
    }

    fun toPipedString(dto: FirebaseBookingDto): String {
        return "${dto.clientName}|${dto.serviceName}|${dto.status}"
    }
}
