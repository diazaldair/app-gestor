package com.gestorplus.appgestor.notification.domain

interface NotificationRepository {
    suspend fun getFCMToken(): String?
    suspend fun handleReceivedNotification(notification: PushNotification)
}
