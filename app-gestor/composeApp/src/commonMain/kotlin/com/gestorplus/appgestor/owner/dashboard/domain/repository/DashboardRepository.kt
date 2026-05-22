package com.gestorplus.appgestor.owner.dashboard.domain.repository

import com.gestorplus.appgestor.owner.dashboard.domain.model.Booking
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun getBookings(): Flow<List<Booking>>
    suspend fun addBooking(booking: Booking)
    suspend fun updateStatus(bookingId: String, newStatus: String)
    suspend fun syncAllBookings()
    suspend fun getFirebaseLogs(path: String): List<String>
}
