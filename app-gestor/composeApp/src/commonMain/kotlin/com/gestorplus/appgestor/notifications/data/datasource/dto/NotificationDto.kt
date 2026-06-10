package com.gestorplus.appgestor.notifications.data.datasource.dto

import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val timestamp: Long? = null,
    val type: String? = null,
    val patientName: String? = null,
    val specialty: String? = null,
    val appointmentDate: String? = null,
    val appointmentTime: String? = null,
    val isRead: Boolean? = false,
    val bookingId: String? = null,
    val patientUid: String? = null,
    val clinicId: String? = null,
    val date: Int? = null,
    val timeSlot: String? = null
)
