package com.gestorplus.appgestor.booking.domain.usecase

import com.gestorplus.appgestor.booking.domain.model.BookingSlot
import com.gestorplus.appgestor.booking.domain.model.SlotPeriod
import com.gestorplus.appgestor.booking.domain.repository.BookingRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FakeBookingRepository : BookingRepository {
    private val slots = mutableListOf<BookingSlot>()
    var shouldReturnError = false

    fun setSlots(newSlots: List<BookingSlot>) {
        slots.clear()
        slots.addAll(newSlots)
    }

    override suspend fun getAvailableSlots(clinicId: String, date: Int): List<BookingSlot> {
        return slots.filter { it.isAvailable }
    }

    override suspend fun confirmBooking(
        clinicId: String, serviceId: String, clinicName: String, serviceName: String,
        doctorName: String, patientName: String, date: Int, month: String,
        timeSlot: String, price: Double, notes: String
    ): Result<Unit> {
        return if (shouldReturnError) {
            Result.failure(Exception("Network error"))
        } else {
            Result.success(Unit)
        }
    }
}

class BookingUseCaseTest {

    private val fakeRepository = FakeBookingRepository()
    private val getAvailableSlotsUseCase = GetAvailableSlotsUseCase(fakeRepository)
    private val confirmBookingUseCase = ConfirmBookingUseCase(fakeRepository)

    @Test
    fun `getAvailableSlots returns filtered available slots`() = runTest {
        val allSlots = listOf(
            BookingSlot("1", "09:00", true, SlotPeriod.MORNING),
            BookingSlot("2", "10:00", false, SlotPeriod.MORNING),
            BookingSlot("3", "15:00", true, SlotPeriod.AFTERNOON)
        )
        fakeRepository.setSlots(allSlots)

        val result = getAvailableSlotsUseCase("clinic-1", 20231027)

        assertEquals(2, result.size)
        assertTrue(result.all { it.isAvailable })
    }

    @Test
    fun `confirmBooking returns success when repository succeeds`() = runTest {
        fakeRepository.shouldReturnError = false

        val result = confirmBookingUseCase(
            "c1", "s1", "Clinic", "Service", "Doctor", "Patient",
            27, "Oct", "09:00 AM", 100.0, "Notes"
        )

        assertTrue(result.isSuccess)
    }
}
