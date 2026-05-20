package com.gestorplus.appgestor.owner.domain.repository

import com.gestorplus.appgestor.owner.domain.model.Booking
import com.gestorplus.appgestor.owner.domain.model.WorkspaceProfile
import kotlinx.coroutines.flow.Flow

interface OwnerRepository {
    fun getBookings(): Flow<List<Booking>>
    suspend fun addBooking(booking: Booking)
    suspend fun updateStatus(bookingId: String, newStatus: String)
    suspend fun syncAllBookings()
    suspend fun initializeAndSyncConfig(defaults: Map<String, String>)
    suspend fun getFirebaseLogs(path: String): List<String>
    suspend fun saveWorkspaceProfile(profile: WorkspaceProfile): Result<Unit>
}
