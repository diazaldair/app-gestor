package com.gestorplus.appgestor.notifications.data.datasource.mapper

import com.gestorplus.appgestor.notifications.data.datasource.dto.NotificationDto
import com.gestorplus.appgestor.notifications.domain.model.AppNotification
import com.gestorplus.appgestor.notifications.domain.model.NotificationType

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
            isRead = dto.isRead ?: false,
            bookingId = dto.bookingId,
            patientUid = dto.patientUid,
            clinicId = dto.clinicId,
            date = dto.date,
            timeSlot = dto.timeSlot
        )
    }

    private fun calculateDateCategory(timestamp: Long): String {
        return if (timestamp > System.currentTimeMillis() - 86400000) "TODAY" else "YESTERDAY"
    }
}
