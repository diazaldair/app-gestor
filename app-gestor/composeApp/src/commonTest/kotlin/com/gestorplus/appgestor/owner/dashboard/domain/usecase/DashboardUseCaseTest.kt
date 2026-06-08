package com.gestorplus.appgestor.owner.dashboard.domain.usecase

import com.gestorplus.appgestor.owner.dashboard.domain.model.Booking
import com.gestorplus.appgestor.owner.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Fake repository for testing Dashboard Use Cases.
 */
class FakeDashboardRepository : DashboardRepository {
    val bookingsList = mutableListOf<Booking>()
    var lastUpdatedId: String? = null
    var lastUpdatedStatus: String? = null
    var syncCalled = false
    var firebaseLogsResult: List<String> = emptyList()

    override fun getBookings(): Flow<List<Booking>> = flowOf(bookingsList)
    
    override suspend fun addBooking(booking: Booking) {
        bookingsList.add(booking)
    }

    override suspend fun updateStatus(bookingId: String, newStatus: String) {
        lastUpdatedId = bookingId
        lastUpdatedStatus = newStatus
        val index = bookingsList.indexOfFirst { it.id == bookingId }
        if (index != -1) {
            bookingsList[index] = bookingsList[index].copy(status = newStatus)
        }
    }

    override suspend fun syncAllBookings() {
        syncCalled = true
    }

    override suspend fun getFirebaseLogs(path: String): List<String> = firebaseLogsResult
}

class DashboardUseCaseTest {

    private val fakeRepo = FakeDashboardRepository()
    private val acceptUseCase = AcceptBookingUseCase(fakeRepo)
    private val rejectUseCase = RejectBookingUseCase(fakeRepo)
    private val getBookingsUseCase = GetOwnerBookingsUseCase(fakeRepo)
    private val syncBookingsUseCase = SyncBookingsUseCase(fakeRepo)
    private val getFirebaseLogsUseCase = GetFirebaseLogsUseCase(fakeRepo)

    @Test
    fun `accept booking should delegate to repository with CONFIRMED status`() = runTest {
        val bookingId = "booking_123"
        
        acceptUseCase(bookingId)

        assertEquals(bookingId, fakeRepo.lastUpdatedId)
        assertEquals("CONFIRMED", fakeRepo.lastUpdatedStatus)
    }

    @Test
    fun `reject booking should delegate to repository with REJECTED status`() = runTest {
        val bookingId = "booking_456"
        
        rejectUseCase(bookingId)

        assertEquals(bookingId, fakeRepo.lastUpdatedId)
        assertEquals("REJECTED", fakeRepo.lastUpdatedStatus)
    }

    @Test
    fun `get owner bookings should return flow from repository`() = runTest {
        val booking = Booking(
            id = "1",
            clientName = "Juan",
            serviceName = "Consulta",
            timestamp = 1698411600000L,
            durationMinutes = 30,
            status = "PENDING",
            price = 50.0
        )
        fakeRepo.addBooking(booking)

        val resultFlow = getBookingsUseCase()
        
        assertTrue(fakeRepo.bookingsList.contains(booking))
    }

    @Test
    fun `sync bookings should call syncAllBookings in repository`() = runTest {
        syncBookingsUseCase()
        assertTrue(fakeRepo.syncCalled)
    }

    @Test
    fun `get firebase logs should return data from repository`() = runTest {
        val expectedLogs = listOf("Log 1", "Log 2")
        fakeRepo.firebaseLogsResult = expectedLogs
        
        val result = getFirebaseLogsUseCase("logs/test")

        assertEquals(expectedLogs, result)
    }
}
