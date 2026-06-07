package com.gestorplus.appgestor.booking.data.datasource.datasource

import com.gestorplus.appgestor.booking.data.datasource.service.BookingService

class BookingRemoteDatasource(private val bookingService: BookingService) {
    suspend fun getAvailableSlots(date: Int): Map<String, Any>? {
        return bookingService.getAvailableSlots(date)
    }

    suspend fun confirmBooking(
        date: Int,
        slot: String,
        notes: String,
        service: String,
        doctor: String,
        totalPrice: Double
    ) {
        val bookingData = mapOf(
            "date" to date,
            "slot" to slot,
            "notes" to notes,
            "service" to service,
            "doctor" to doctor,
            "totalPrice" to totalPrice,
            "status" to "PENDING",
            "timestamp" to 123456789L // Use a proper timestamp if possible
        )
        bookingService.saveBooking(date, slot, bookingData)
    }
}
