package com.gestorplus.appgestor.domain.notification

interface NotificationRepository {
    suspend fun getFCMToken(): String?
    suspend fun handleReceivedNotification(notification: PushNotification)
}
