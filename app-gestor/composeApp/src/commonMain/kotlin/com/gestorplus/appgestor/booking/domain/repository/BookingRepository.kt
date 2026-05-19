package com.gestorplus.appgestor.booking.domain.repository

import com.gestorplus.appgestor.booking.domain.model.BookingSlot

interface BookingRepository {
    suspend fun getAvailableSlots(date: Int): List<BookingSlot>
    suspend fun confirmBooking(date: Int, slot: String): Result<Unit>
}
