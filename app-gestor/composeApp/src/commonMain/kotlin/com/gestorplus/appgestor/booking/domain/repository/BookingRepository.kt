package com.gestorplus.appgestor.booking.domain.repository

import com.gestorplus.appgestor.booking.domain.model.BookingSlot

interface BookingRepository {
    suspend fun getAvailableSlots(date: Int): List<BookingSlot>
    suspend fun confirmBooking(
        clinicId: String,
        serviceId: String,
        clinicName: String,
        serviceName: String,
        price: Double,
        date: Int,
        month: String,
        timeSlot: String
    ): Result<Unit>
}
