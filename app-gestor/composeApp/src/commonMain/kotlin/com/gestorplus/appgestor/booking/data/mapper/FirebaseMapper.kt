package com.gestorplus.appgestor.booking.data.mapper

import com.gestorplus.appgestor.booking.data.datasource.dto.FirebaseBookingDto
import com.gestorplus.appgestor.booking.domain.model.SlotPeriod

/**
 * Mapper to handle parsing and formatting of Firebase Realtime Database strings.
 * Pattern: "clientName|serviceName|status" or "time|isAvailable|period"
 */
class FirebaseMapper {

    /**
     * Parses a piped string from Firebase into a DTO.
     * Expected format for Owner: "Client Name|Service Name|STATUS"
     */
    fun parseBooking(value: String): FirebaseBookingDto {
        if (value.isBlank()) return FirebaseBookingDto()
        
        val parts = value.split("|")
        return FirebaseBookingDto(
            clientName = parts.getOrNull(0)?.takeIf { it.isNotBlank() } ?: "Unknown",
            serviceName = parts.getOrNull(1)?.takeIf { it.isNotBlank() } ?: "General Service",
            status = parts.getOrNull(2)?.takeIf { it.isNotBlank() } ?: "PENDING"
        )
    }

    /**
     * Parses a piped string from Firebase into a DTO for available slots.
     * Expected format for Client: "09:00 AM|true|MORNING"
     */
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

    /**
     * Formats a DTO into a piped string for saving to Firebase.
     */
    fun toPipedString(dto: FirebaseBookingDto): String {
        return "${dto.clientName}|${dto.serviceName}|${dto.status}"
    }
}
