package com.gestorplus.appgestor.booking.data.datasource.repository

import com.gestorplus.appgestor.booking.data.datasource.datasource.BookingRemoteDatasource
import com.gestorplus.appgestor.booking.data.datasource.mapper.BookingMapper
import com.gestorplus.appgestor.booking.domain.model.BookingSlot
import com.gestorplus.appgestor.booking.domain.model.SlotPeriod
import com.gestorplus.appgestor.booking.domain.repository.BookingRepository

class BookingRepositoryImpl(
    private val bookingRemoteDatasource: BookingRemoteDatasource,
    private val bookingMapper: BookingMapper
) : BookingRepository {

    override suspend fun getAvailableSlots(clinicId: String, date: Int): List<BookingSlot> {
        val remoteSlots = bookingRemoteDatasource.getAvailableSlots(clinicId, date)
        
        val bookedSlots = remoteSlots?.keys ?: emptySet()

        return getDefaultSlots().map { slot ->
            if (bookedSlots.contains(slot.time.replace(" ", "_"))) {
                slot.copy(isAvailable = false)
            } else {
                slot
            }
        }
    }

    override suspend fun confirmBooking(
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
    ): Result<Unit> {
        return try {
            bookingRemoteDatasource.confirmBooking(
                clinicId = clinicId,
                serviceId = serviceId,
                clinicName = clinicName,
                serviceName = serviceName,
                doctorName = doctorName,
                patientName = patientName,
                date = date,
                month = month,
                timeSlot = timeSlot,
                price = price,
                notes = notes
            )
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
