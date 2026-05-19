package com.gestorplus.appgestor.data.repository

import com.gestorplus.appgestor.data.local.dao.BookingDao
import com.gestorplus.appgestor.data.local.entity.BookingEntity
import com.gestorplus.appgestor.data.datasource.FirebaseManager
import com.gestorplus.appgestor.core.util.DateTimeUtils
import com.gestorplus.appgestor.data.mapper.FirebaseMapper
import com.gestorplus.appgestor.data.booking.model.FirebaseBookingDto
import kotlinx.coroutines.flow.Flow

class OwnerBookingRepository(
    private val bookingDao: BookingDao,
    private val firebaseManager: FirebaseManager,
    private val firebaseMapper: FirebaseMapper
) {
    fun getBookings(): Flow<List<BookingEntity>> = bookingDao.getAllBookings()

    suspend fun addBooking(booking: BookingEntity) {
        bookingDao.insertBooking(booking)
        
        try {
            val dto = FirebaseBookingDto(
                clientName = booking.clientName,
                serviceName = booking.serviceName,
                status = booking.status
            )
            val dataPath = "bookings/${booking.id}"
            firebaseManager.saveData(dataPath, firebaseMapper.toPipedString(dto))
        } catch (e: Exception) {
            // Offline-first: already saved in Room
        }
    }

    suspend fun updateStatus(bookingId: String, newStatus: String) {
        bookingDao.updateBookingStatus(bookingId, newStatus)
        // Note: For simplicity, update only status field in Firebase if path matches
        firebaseManager.saveData("bookings/$bookingId/status", newStatus)
    }

    suspend fun syncAllBookings() {
        try {
            val allRemoteData = firebaseManager.getData("bookings") ?: return
            
            allRemoteData.forEach { (date, slots) ->
                (slots as? Map<String, String>)?.forEach { (slotId, value) ->
                    val dto = firebaseMapper.parseBooking(value)
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
                    bookingDao.insertBooking(booking)
                }
            }
        } catch (e: Exception) {
            // Log error or handle failure
        }
    }

    suspend fun syncInitialConfig() {
        try {
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
                bookingDao.insertBooking(configBooking)
            }
        } catch (e: Exception) {
            // Keep local data if fetch fails
        }
    }
}
