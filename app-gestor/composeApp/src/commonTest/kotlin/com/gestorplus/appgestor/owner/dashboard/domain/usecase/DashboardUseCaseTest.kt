package com.gestorplus.appgestor.owner.dashboard.domain.usecase

import com.gestorplus.appgestor.owner.dashboard.domain.model.Booking
import com.gestorplus.appgestor.owner.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FakeDashboardRepository : DashboardRepository {
    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    var syncCalled = false
    val statusUpdates = mutableMapOf<String, String>()

    override fun getBookings(): Flow<List<Booking>> = _bookings

    override suspend fun addBooking(booking: Booking) {
        _bookings.value = _bookings.value + booking
    }

    override suspend fun updateStatus(bookingId: String, newStatus: String) {
        statusUpdates[bookingId] = newStatus
        _bookings.value = _bookings.value.map {
            if (it.id == bookingId) it.copy(status = newStatus) else it
        }
    }

    override suspend fun syncAllBookings() {
        syncCalled = true
    }

    override suspend fun getFirebaseLogs(path: String): List<String> = emptyList()
}

class DashboardUseCaseTest {

    private val fakeRepository = FakeDashboardRepository()
    private val acceptBookingUseCase = AcceptBookingUseCase(fakeRepository)
    private val rejectBookingUseCase = RejectBookingUseCase(fakeRepository)
    private val getOwnerBookingsUseCase = GetOwnerBookingsUseCase(fakeRepository)
    private val syncBookingsUseCase = SyncBookingsUseCase(fakeRepository)

    @Test
    fun `acceptBooking updates status to CONFIRMED`() = runTest {
        val bookingId = "123"
        acceptBookingUseCase(bookingId)
        assertEquals("CONFIRMED", fakeRepository.statusUpdates[bookingId])
    }

    @Test
    fun `rejectBooking updates status to REJECTED`() = runTest {
        val bookingId = "456"
        rejectBookingUseCase(bookingId)
        assertEquals("REJECTED", fakeRepository.statusUpdates[bookingId])
    }

    @Test
    fun `syncBookings calls repository sync`() = runTest {
        syncBookingsUseCase()
        assertTrue(fakeRepository.syncCalled)
    }

    @Test
    fun `getOwnerBookings returns stream from repository`() = runTest {
        val booking = Booking("1", "Client", "Service", 0L, 30, "PENDING", 100.0)
        fakeRepository.addBooking(booking)

        val result = getOwnerBookingsUseCase().first()
        assertEquals(1, result.size)
        assertEquals("Client", result[0].clientName)
    }
}
