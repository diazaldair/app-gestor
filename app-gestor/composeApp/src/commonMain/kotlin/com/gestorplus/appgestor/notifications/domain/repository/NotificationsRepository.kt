package com.gestorplus.appgestor.notifications.domain.repository

import com.gestorplus.appgestor.notifications.domain.model.AppNotification
import kotlinx.coroutines.flow.Flow

interface NotificationsRepository {
    fun getNotifications(): Flow<List<AppNotification>>
    suspend fun acceptAppointment(notificationId: String)
    suspend fun declineAppointment(notificationId: String, reason: String? = null)
    suspend fun markAsRead(notificationId: String)
}
