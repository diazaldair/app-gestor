package com.gestorplus.appgestor.notifications.domain.model

enum class NotificationType {
    APPOINTMENT_REQUEST,
    REMINDER,
    SYSTEM_UPDATE
}

data class AppNotification(
    val id: String,
    val title: String,
    val description: String,
    val timestamp: Long,
    val type: NotificationType,
    val dateCategory: String, // "TODAY", "YESTERDAY"
    val patientName: String? = null,
    val specialty: String? = null,
    val appointmentDate: String? = null,
    val appointmentTime: String? = null,
    val timeRemaining: String? = null,
    val isRead: Boolean = false,
    val bookingId: String? = null,
    val patientUid: String? = null,
    val clinicId: String? = null,
    val date: Int? = null,
    val timeSlot: String? = null
)
