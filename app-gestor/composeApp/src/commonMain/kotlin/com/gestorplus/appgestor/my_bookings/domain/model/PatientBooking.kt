package com.gestorplus.appgestor.my_bookings.domain.model

data class PatientBooking(
    val id: String,
    val clinicId: String,
    val clinicName: String,
    val serviceName: String,
    val doctorName: String,
    val doctorImageUrl: String?,
    val timestamp: Long,
    val status: BookingStatus,
    val price: Double,
    val currency: String,
    val date: Int? = null,
    val month: String? = null,
    val timeSlot: String? = null,
    val notes: String? = null
)

enum class BookingStatus {
    PENDING,
    REVIEWING,
    CONFIRMED,
    CANCELLED,
    COMPLETED
}
