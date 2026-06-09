package com.gestorplus.appgestor.notifications.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val timestamp: Long,
    val type: String,
    val patientName: String?,
    val specialty: String?,
    val appointmentDate: String?,
    val appointmentTime: String?,
    val isRead: Boolean
)
