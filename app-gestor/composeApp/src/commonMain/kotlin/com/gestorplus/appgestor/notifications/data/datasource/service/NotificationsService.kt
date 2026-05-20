package com.gestorplus.appgestor.notifications.data.datasource.service

import com.gestorplus.appgestor.notifications.data.datasource.dto.NotificationDto

class NotificationsService(private val client: Any) {
    suspend fun getNotifications(): List<NotificationDto> {
        return emptyList()
    }
}
