package com.gestorplus.appgestor.owner.home.domain.model

data class Appointment(
    val id: String,
    val clientName: String,
    val serviceType: String,
    val timeLabel: String,        // "09:30 AM"
    val countdownLabel: String,   // "EN 15 MIN"
    val isNext: Boolean = false,
    val status: AppointmentStatus = AppointmentStatus.SCHEDULED
)

enum class AppointmentStatus {
    SCHEDULED, FREE_SLOT, COMPLETED, CANCELLED
}
