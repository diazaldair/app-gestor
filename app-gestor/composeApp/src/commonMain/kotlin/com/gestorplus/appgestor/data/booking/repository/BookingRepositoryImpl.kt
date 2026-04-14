package com.gestorplus.appgestor.data.booking.repository

import com.gestorplus.appgestor.domain.booking.model.BookingSlot
import com.gestorplus.appgestor.domain.booking.model.SlotPeriod
import com.gestorplus.appgestor.domain.booking.repository.BookingRepository
import kotlinx.coroutines.delay

class BookingRepositoryImpl : BookingRepository {

    override suspend fun getAvailableSlots(date: Int): List<BookingSlot> {
        delay(500) // Simulate network delay
        return listOf(
            BookingSlot("1", "09:00 AM", period = SlotPeriod.MORNING),
            BookingSlot("2", "09:30 AM", period = SlotPeriod.MORNING),
            BookingSlot("3", "10:00 AM", period = SlotPeriod.MORNING),
            BookingSlot("4", "10:30 AM", period = SlotPeriod.MORNING),
            BookingSlot("5", "11:00 AM", period = SlotPeriod.MORNING),
            BookingSlot("6", "11:30 AM", period = SlotPeriod.MORNING),
            BookingSlot("7", "01:00 PM", period = SlotPeriod.AFTERNOON),
            BookingSlot("8", "01:30 PM", period = SlotPeriod.AFTERNOON),
            BookingSlot("9", "02:00 PM", period = SlotPeriod.AFTERNOON),
            BookingSlot("10", "02:30 PM", period = SlotPeriod.AFTERNOON),
            BookingSlot("11", "03:00 PM", period = SlotPeriod.AFTERNOON),
            BookingSlot("12", "04:00 PM", period = SlotPeriod.AFTERNOON)
        )
    }

    override suspend fun confirmBooking(date: Int, slot: String): Result<Unit> {
        delay(1000) // Simulate network delay
        return Result.success(Unit)
    }
}
