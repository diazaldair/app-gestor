package com.gestorplus.appgestor.notifications.data.datasource.mapper

import com.gestorplus.appgestor.notifications.data.datasource.dto.NotificationDto
import com.gestorplus.appgestor.notifications.domain.model.AppNotification
import com.gestorplus.appgestor.notifications.domain.model.NotificationType
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class NotificationMapper {
    fun toDomain(dto: NotificationDto): AppNotification {
        val type = when (dto.type) {
            "APPOINTMENT_REQUEST" -> NotificationType.APPOINTMENT_REQUEST
            "REMINDER" -> NotificationType.REMINDER
            else -> NotificationType.SYSTEM_UPDATE
        }
        
        return AppNotification(
            id = dto.id ?: "",
            title = dto.title ?: "",
            description = dto.description ?: "",
            timestamp = dto.timestamp ?: 0L,
            type = type,
            dateCategory = calculateDateCategory(dto.timestamp ?: 0L),
            patientName = dto.patientName,
            specialty = dto.specialty,
            appointmentDate = dto.appointmentDate,
            appointmentTime = dto.appointmentTime,
            isRead = dto.isRead ?: false
        )
    }

    private fun calculateDateCategory(timestamp: Long): String {
        // Simplified logic for demo
        return if (timestamp > System.currentTimeMillis() - 86400000) "TODAY" else "YESTERDAY"
    }
}
