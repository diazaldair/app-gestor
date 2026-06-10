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

    suspend fun updateAppointmentStatus(
        userUid: String, 
        notificationId: String, 
        status: String,
        bookingId: String?,
        patientUid: String?,
        clinicId: String?,
        date: Int?,
        timeSlot: String?
    ) {
        // 1. Actualizar estado en la notificación
        firebaseManager.saveData("notifications/$userUid/$notificationId/status", status)

        if (bookingId != null && patientUid != null && clinicId != null && date != null && timeSlot != null) {
            val finalStatus = if (status == "ACCEPTED") "CONFIRMED" else "DECLINED"
            val slotKey = timeSlot.replace(" ", "_")

            // 2. Actualizar en la agenda de la clínica
            firebaseManager.saveData("workspaces/$clinicId/appointments/$date/$slotKey/status", finalStatus)

            // 3. Actualizar en las reservas del paciente
            firebaseManager.saveData("users/$patientUid/bookings/$bookingId/status", finalStatus)
        }
    }

    suspend fun markAsRead(userUid: String, notificationId: String) {
        firebaseManager.saveData("notifications/$userUid/$notificationId/isRead", true)
    }
}
