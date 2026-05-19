package com.gestorplus.appgestor.booking.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class AppointmentModel(
    val id: String = "",
    val serviceName: String,
    val date: String,
    val time: String,
    val location: String,
    val totalCost: Double,
    val doctorName: String,
    val doctorImage: String? = null,
    val status: AppointmentStatus = AppointmentStatus.PENDING
)

enum class AppointmentStatus {
    PENDING,
    REVIEWING,
    CONFIRMED,
    CANCELLED
}
