package com.gestorplus.appgestor.domain.booking.usecase

import com.gestorplus.appgestor.booking.domain.model.BookingSlot
import com.gestorplus.appgestor.booking.domain.model.SlotPeriod
import com.gestorplus.appgestor.booking.domain.repository.BookingRepository
import com.gestorplus.appgestor.booking.domain.usecase.ConfirmBookingUseCase
import com.gestorplus.appgestor.booking.domain.usecase.GetAvailableSlotsUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Fake implementation of BookingRepository for testing purposes.
 * This avoids dependencies on Room or Firebase.
 */
class FakeBookingRepository : BookingRepository {
    private val slots = mutableListOf<BookingSlot>()
    var shouldReturnError = false

    fun setSlots(newSlots: List<BookingSlot>) {
        slots.clear()
        slots.addAll(newSlots)
    }

    override suspend fun getAvailableSlots(date: Int): List<BookingSlot> {
        return slots.filter { it.isAvailable }
    }

    override suspend fun confirmBooking(date: Int, slot: String): Result<Unit> {
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
        // Arrange
        val allSlots = listOf(
            BookingSlot("1", "09:00", true, SlotPeriod.MORNING),
            BookingSlot("2", "10:00", false, SlotPeriod.MORNING),
            BookingSlot("3", "15:00", true, SlotPeriod.AFTERNOON)
        )
        fakeRepository.setSlots(allSlots)

        // Act
        val result = getAvailableSlotsUseCase(20231027)

        // Assert
        assertEquals(2, result.size)
        assertTrue(result.all { it.isAvailable })
    }

    @Test
    fun `getAvailableSlots returns empty list when no slots are available`() = runTest {
        // Arrange
        fakeRepository.setSlots(emptyList())

        // Act
        val result = getAvailableSlotsUseCase(20231027)

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `confirmBooking returns success when repository succeeds`() = runTest {
        // Arrange
        fakeRepository.shouldReturnError = false

        // Act
        val result = confirmBookingUseCase(20231027, "09:00")

        // Assert
        assertTrue(result.isSuccess)
    }

    @Test
    fun `confirmBooking returns failure when repository fails`() = runTest {
        // Arrange
        fakeRepository.shouldReturnError = true

        // Act
        val result = confirmBookingUseCase(20231027, "09:00")

        // Assert
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}
