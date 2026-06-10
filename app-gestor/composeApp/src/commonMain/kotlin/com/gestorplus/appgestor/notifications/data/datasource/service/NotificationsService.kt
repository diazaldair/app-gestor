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

    suspend fun updateAppointmentStatus(userUid: String, notificationId: String, status: String, reason: String? = null) {
        // 1. Actualizar estado en la notificación del Dr
        firebaseManager.saveData("notifications/$userUid/$notificationId/appointmentStatus", status)
        
        // 2. Actualizar estado en la cita del workspace (Esto dispara la Cloud Function)
        firebaseManager.saveData("workspaces/$userUid/appointments/$notificationId/status", status)
        
        // 3. Sincronizar con la vista del paciente
        val appointmentData = firebaseManager.getData("workspaces/$userUid/appointments/$notificationId")
        val patientId = (appointmentData as? Map<String, Any>)?.get("patientId") as? String
        
        if (patientId != null) {
            firebaseManager.saveData("users/$patientId/bookings/$notificationId/status", status)
            if (reason != null) {
                firebaseManager.saveData("users/$patientId/bookings/$notificationId/declineReason", reason)
                firebaseManager.saveData("workspaces/$userUid/appointments/$notificationId/declineReason", reason)
            }
        }
    }

    suspend fun markAsRead(userUid: String, notificationId: String) {
        firebaseManager.saveData("notifications/$userUid/$notificationId/isRead", "true")
    }
}
