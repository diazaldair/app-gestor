package com.gestorplus.appgestor.notifications.domain.repository

import com.gestorplus.appgestor.notifications.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationsRepository {
    fun getNotifications(): Flow<List<Notification>>
    suspend fun acceptAppointment(notificationId: String)
    suspend fun declineAppointment(notificationId: String)
}
