package com.gestorplus.appgestor.booking.data.repository

import com.gestorplus.appgestor.data.datasource.FirebaseManager
import com.gestorplus.appgestor.booking.data.mapper.BookingMapper
import com.gestorplus.appgestor.booking.data.dto.FirebaseBookingDto
import com.gestorplus.appgestor.booking.domain.model.BookingSlot
import com.gestorplus.appgestor.booking.domain.model.SlotPeriod
import com.gestorplus.appgestor.booking.domain.repository.BookingRepository

class BookingRepositoryImpl(
    private val firebaseManager: FirebaseManager,
    private val bookingMapper: BookingMapper
) : BookingRepository {

    override suspend fun getAvailableSlots(date: Int): List<BookingSlot> {
        val remoteSlots = firebaseManager.getData("available_slots/$date")
        
        if (remoteSlots != null) {
            return remoteSlots.map { (id, value) ->
                val dto = bookingMapper.parseSlot(value.toString())
                bookingMapper.toDomain(id, dto)
            }
        }

        // Mock data fallback if no data in Firebase
        return getDefaultSlots()
    }

    override suspend fun confirmBooking(date: Int, slot: String): Result<Unit> {
        return try {
            val dto = FirebaseBookingDto(status = "CONFIRMED")
            firebaseManager.saveData("bookings/$date/$slot", "CLIENT_ID_MOCK|CONFIRMED")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getDefaultSlots() = listOf(
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
