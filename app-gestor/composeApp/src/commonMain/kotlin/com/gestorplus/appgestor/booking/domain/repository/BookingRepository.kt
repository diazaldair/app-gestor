package com.gestorplus.appgestor.booking.domain.repository

import com.gestorplus.appgestor.booking.domain.model.BookingSlot

interface BookingRepository {
    suspend fun getAvailableSlots(clinicId: String, date: Int): List<BookingSlot>
    suspend fun confirmBooking(
        clinicId: String,
        serviceId: String,
        clinicName: String,
        serviceName: String,
        doctorName: String,
        patientName: String,
        date: Int,
        month: String,
        timeSlot: String,
        price: Double,
        notes: String
    ): Result<Unit>
}
