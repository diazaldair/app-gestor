package com.gestorplus.appgestor.owner.data.datasource.repository

import com.gestorplus.appgestor.booking.data.datasource.dto.FirebaseBookingDto
import com.gestorplus.appgestor.core.util.DateTimeUtils
import com.gestorplus.appgestor.data.local.entity.BookingEntity
import com.gestorplus.appgestor.data.datasource.FirebaseManager
import com.gestorplus.appgestor.owner.data.datasource.datasource.OwnerLocalDatasource
import com.gestorplus.appgestor.owner.data.datasource.datasource.OwnerRemoteDatasource
import com.gestorplus.appgestor.owner.data.datasource.mapper.OwnerMapper
import com.gestorplus.appgestor.owner.domain.model.Booking
import com.gestorplus.appgestor.owner.domain.repository.OwnerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OwnerBookingRepository(
    private val localDatasource: OwnerLocalDatasource,
    private val remoteDatasource: OwnerRemoteDatasource,
    private val firebaseManager: FirebaseManager, // For RemoteConfig and log fetching
    private val ownerMapper: OwnerMapper
) : OwnerRepository {

    override fun getBookings(): Flow<List<Booking>> {
        return localDatasource.getBookingsFlow().map { entities ->
            entities.map { ownerMapper.toDomain(it) }
        }
    }

    override suspend fun addBooking(booking: Booking) {
        val entity = ownerMapper.toEntity(booking)
        localDatasource.saveBookings(listOf(entity))
        
        try {
            val dto = FirebaseBookingDto(
                clientName = booking.clientName,
                serviceName = booking.serviceName,
                status = booking.status
            )
            remoteDatasource.updateBooking(booking.id.split("-").first().toIntOrNull() ?: 0, booking.id.split("-").last(), ownerMapper.toPipedString(dto))
        } catch (e: Exception) {
            // Offline-first: already saved in Room
        }
    }

    override suspend fun updateStatus(bookingId: String, newStatus: String) {
        localDatasource.updateBookingStatus(bookingId, newStatus)
        val parts = bookingId.split("-")
        if (parts.size >= 2) {
            val date = parts[0].toIntOrNull() ?: 0
            val slot = parts[1]
            firebaseManager.saveData("bookings/$date/$slot/status", newStatus)
        } else {
            firebaseManager.saveData("bookings/$bookingId/status", newStatus)
        }
    }

    override suspend fun syncAllBookings() {
        try {
            val bookingsRoot = firebaseManager.getData("bookings") ?: return
            
            bookingsRoot.forEach { (date, slots) ->
                (slots as? Map<String, String>)?.forEach { (slotId, value) ->
                    val dto = ownerMapper.parseBooking(value)
                    val slotIndex = slotId.toIntOrNull() ?: 0
                    val dateInt = date.toIntOrNull() ?: 0
                    val booking = BookingEntity(
                        id = "$date-$slotId",
                        clientName = dto.clientName,
                        serviceName = dto.serviceName,
                        timestamp = DateTimeUtils.calculateTimestamp(dateInt, slotIndex),
                        durationMinutes = 30,
                        status = dto.status,
                        price = 0.0,
                        categoryColor = 0xFF6200EE
                    )
                    localDatasource.saveBookings(listOf(booking))
                }
            }
        } catch (e: Exception) {
            // Log error or handle failure
        }
    }

    override suspend fun initializeAndSyncConfig(defaults: Map<String, String>) {
        try {
            firebaseManager.initializeRemoteConfig(defaults)
            firebaseManager.fetchAndActivate()
            
            val syncClient = firebaseManager.getString("sync_client_name")
            val syncService = firebaseManager.getString("sync_client_service")
            val syncPrice = firebaseManager.getString("sync_price").toDoubleOrNull() ?: 99.9

            if (syncClient.isNotEmpty() && syncClient != "vacio") {
                val configBooking = BookingEntity(
                    id = "config_sync_001",
                    clientName = syncClient,
                    serviceName = syncService,
                    timestamp = System.currentTimeMillis(),
                    durationMinutes = 45,
                    status = "FEATURED",
                    price = syncPrice,
                    categoryColor = 0xFF00BCD4
                )
                localDatasource.saveBookings(listOf(configBooking))
            }
        } catch (e: Exception) {
            // Keep local data if fetch fails
        }
    }

    override suspend fun getFirebaseLogs(path: String): List<String> {
        return firebaseManager.getFirebaseLogs(path)
    }
}
