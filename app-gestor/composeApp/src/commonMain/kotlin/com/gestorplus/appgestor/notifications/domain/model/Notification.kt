package com.gestorplus.appgestor.notifications.domain.model

enum class NotificationType {
    APPOINTMENT_REQUEST,
    REMINDER,
    SYSTEM_UPDATE
}

data class Notification(
    val id: String,
    val title: String,
    val description: String,
    val time: String,
    val type: NotificationType,
    val dateCategory: String // "HOY", "AYER", etc.
)
