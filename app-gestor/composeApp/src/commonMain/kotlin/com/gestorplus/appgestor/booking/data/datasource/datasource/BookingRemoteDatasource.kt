package com.gestorplus.appgestor.booking.data.datasource.datasource

import com.gestorplus.appgestor.booking.data.datasource.service.BookingService

class BookingRemoteDatasource(private val bookingService: BookingService) {
    suspend fun getAvailableSlots(date: Int): Map<String, Any>? {
        return bookingService.getAvailableSlots(date)
    }

    suspend fun confirmBooking(date: Int, slot: String) {
        bookingService.saveBooking(date, slot, "CLIENT_ID_MOCK|CONFIRMED")
    }
}
