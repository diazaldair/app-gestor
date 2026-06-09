package com.gestorplus.appgestor.notifications.data.datasource.service

import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import com.gestorplus.appgestor.notifications.data.datasource.dto.NotificationDto
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class NotificationsService(private val firebaseManager: FirebaseManager) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getNotifications(userUid: String): List<NotificationDto> {
        val data = firebaseManager.getData("notifications/$userUid") ?: return emptyList()
        return data.values.map { 
            val jsonString = json.encodeToString(it)
            json.decodeFromString<NotificationDto>(jsonString)
        }
    }

    suspend fun updateAppointmentStatus(userUid: String, notificationId: String, status: String) {
        firebaseManager.saveData("notifications/$userUid/$notificationId/appointmentStatus", status)
    }

    suspend fun markAsRead(userUid: String, notificationId: String) {
        firebaseManager.saveData("notifications/$userUid/$notificationId/isRead", "true")
    }
}
