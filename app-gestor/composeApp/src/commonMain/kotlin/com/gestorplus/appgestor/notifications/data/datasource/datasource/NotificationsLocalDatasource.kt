package com.gestorplus.appgestor.notifications.data.datasource.datasource

import com.gestorplus.appgestor.notifications.data.local.dao.NotificationDao
import com.gestorplus.appgestor.notifications.data.local.entity.NotificationEntity
import com.gestorplus.appgestor.notifications.data.datasource.dto.NotificationDto
import kotlinx.coroutines.flow.firstOrNull

class NotificationsLocalDatasource(
    private val notificationDao: NotificationDao
) {
    suspend fun getCachedNotifications(): List<NotificationDto> {
        val entities = notificationDao.getAllNotifications().firstOrNull() ?: emptyList()
        return entities.map { entity ->
            NotificationDto(
                id = entity.id,
                title = entity.title,
                description = entity.description,
                timestamp = entity.timestamp,
                type = entity.type,
                patientName = entity.patientName,
                specialty = entity.specialty,
                appointmentDate = entity.appointmentDate,
                appointmentTime = entity.appointmentTime,
                isRead = entity.isRead,
                appointmentStatus = entity.appointmentStatus,
                declineReason = entity.declineReason
            )
        }
    }

    suspend fun cacheNotifications(notifications: List<NotificationDto>) {
        val entities = notifications.map { dto ->
            NotificationEntity(
                id = dto.id ?: "",
                title = dto.title ?: "",
                description = dto.description ?: "",
                timestamp = dto.timestamp ?: 0L,
                type = dto.type ?: "SYSTEM_UPDATE",
                patientName = dto.patientName,
                specialty = dto.specialty,
                appointmentDate = dto.appointmentDate,
                appointmentTime = dto.appointmentTime,
                isRead = dto.isRead ?: false,
                appointmentStatus = dto.appointmentStatus,
                declineReason = dto.declineReason
            )
        }
        notificationDao.insertNotifications(entities)
    }

    suspend fun markAsRead(id: String) {
        notificationDao.markAsRead(id)
    }

    suspend fun clearCache() {
        notificationDao.clearAll()
    }
}
