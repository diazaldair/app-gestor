package com.gestorplus.appgestor.domain.owner.repository

import com.gestorplus.appgestor.domain.owner.model.Booking
import kotlinx.coroutines.flow.Flow

interface OwnerRepository {
    fun getBookings(): Flow<List<Booking>>
    suspend fun addBooking(booking: Booking)
    suspend fun updateStatus(bookingId: String, newStatus: String)
    suspend fun syncAllBookings()
    suspend fun initializeAndSyncConfig(defaults: Map<String, String>)
    suspend fun getFirebaseLogs(path: String): List<String>
}
