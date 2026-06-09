package com.gestorplus.appgestor.notifications.data.datasource.datasource

import com.gestorplus.appgestor.notifications.data.datasource.dto.NotificationDto

class NotificationsLocalDatasource {
    // Placeholder for Room implementation - logic to be added when Room DAOs are ready
    suspend fun getCachedNotifications(): List<NotificationDto> = emptyList()
    suspend fun cacheNotifications(notifications: List<NotificationDto>) {}
}
