package com.gestorplus.appgestor.domain.booking.repository

import com.gestorplus.appgestor.domain.booking.model.BookingSlot

interface BookingRepository {
    suspend fun getAvailableSlots(date: Int): List<BookingSlot>
    suspend fun confirmBooking(date: Int, slot: String): Result<Unit>
}
