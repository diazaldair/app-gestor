package com.gestorplus.appgestor.owner.dashboard.data.mapper

import com.gestorplus.appgestor.data.local.entity.BookingEntity
import com.gestorplus.appgestor.booking.data.datasource.dto.FirebaseBookingDto
import com.gestorplus.appgestor.owner.dashboard.domain.model.Booking

class DashboardMapper {

    fun toDomain(entity: BookingEntity): Booking {
        return Booking(
            id = entity.id,
            clientName = entity.clientName,
            serviceName = entity.serviceName,
            timestamp = entity.timestamp,
            durationMinutes = entity.durationMinutes,
            status = entity.status,
            price = entity.price
        )
    }

    fun toEntity(domain: Booking, categoryColor: Long = 0xFF6200EE): BookingEntity {
        return BookingEntity(
            id = domain.id,
            clientName = domain.clientName,
            serviceName = domain.serviceName,
            timestamp = domain.timestamp,
            durationMinutes = domain.durationMinutes,
            status = domain.status,
            price = domain.price,
            categoryColor = categoryColor
        )
    }

    fun parseBooking(value: String): FirebaseBookingDto {
        if (value.isBlank()) return FirebaseBookingDto()

        val parts = value.split("|")
        return FirebaseBookingDto(
            clientName = parts.getOrNull(0)?.takeIf { it.isNotBlank() } ?: "Unknown",
            serviceName = parts.getOrNull(1)?.takeIf { it.isNotBlank() } ?: "General Service",
            status = parts.getOrNull(2)?.takeIf { it.isNotBlank() } ?: "PENDING"
        )
    }

    fun toPipedString(dto: FirebaseBookingDto): String {
        return "${dto.clientName}|${dto.serviceName}|${dto.status}"
    }
}
