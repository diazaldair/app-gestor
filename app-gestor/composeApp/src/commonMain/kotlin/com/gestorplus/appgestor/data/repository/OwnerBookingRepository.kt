package com.gestorplus.appgestor.data.repository

import com.gestorplus.appgestor.data.local.dao.BookingDao
import com.gestorplus.appgestor.data.local.entity.BookingEntity
import com.gestorplus.appgestor.data.datasource.FirebaseManager
import kotlinx.coroutines.flow.Flow

class OwnerBookingRepository(
    private val bookingDao: BookingDao,
    private val firebaseManager: FirebaseManager
) {
    // Escuchamos los cambios directamente de Room (Offline-First)
    fun getBookings(): Flow<List<BookingEntity>> = bookingDao.getAllBookings()

    suspend fun addBooking(booking: BookingEntity) {
        // 1. Guardamos en local (Instantáneo para la UI)
        bookingDao.insertBooking(booking)
        
        // 2. Intentamos subir a Firebase
        try {
            // Guardamos un string plano por ahora, pero lo ideal sería serializar a JSON
            val dataPath = "bookings/${booking.id}"
            val dataValue = "${booking.clientName}|${booking.serviceName}|${booking.status}"
            firebaseManager.saveData(dataPath, dataValue)
        } catch (e: Exception) {
            // Si falla la subida (ej. sin internet), ya está guardado en local.
            // Aquí podríamos marcarlo como "pendiente_de_sincronizar"
        }
    }

    suspend fun updateStatus(bookingId: String, newStatus: String) {
        bookingDao.updateBookingStatus(bookingId, newStatus)
        firebaseManager.saveData("bookings/$bookingId/status", newStatus)
    }

    suspend fun syncAllBookings() {
        try {
            // Fetch all bookings for all dates (simplified)
            // En un caso real, filtraríamos por fecha o usaríamos un listener.
            val allRemoteData = firebaseManager.getData("bookings") ?: return
            
            allRemoteData.forEach { (date, slots) ->
                (slots as? Map<String, String>)?.forEach { (slotId, value) ->
                    val parts = value.split("|")
                    val booking = BookingEntity(
                        id = "$date-$slotId",
                        clientName = parts.getOrNull(0) ?: "Unknown",
                        serviceName = parts.getOrNull(1) ?: "General Service",
                        timestamp = System.currentTimeMillis(),
                        durationMinutes = 30,
                        status = parts.getOrNull(2) ?: "PENDING",
                        price = 0.0, // Valor por defecto para sincronización masiva
                        categoryColor = 0xFF6200EE // Color por defecto
                    )
                    bookingDao.insertBooking(booking)
                }
            }
        } catch (e: Exception) {
            // Log error
        }
    }

    suspend fun syncInitialConfig() {
        try {
            // 1. Obtenemos datos de Remote Config
            val syncClient = firebaseManager.getString("sync_client_name")
            val syncService = firebaseManager.getString("sync_client_service")
            val syncPrice = firebaseManager.getString("sync_price").toDoubleOrNull() ?: 99.9

            // 2. Si hay datos válidos, guardamos en Room como "Caché inicial"
            if (syncClient.isNotEmpty() && syncClient != "vacio") {
                val configBooking = BookingEntity(
                    id = "config_sync_001",
                    clientName = syncClient,
                    serviceName = syncService,
                    timestamp = System.currentTimeMillis(),
                    durationMinutes = 45,
                    status = "FEATURED", // Un estado especial para identificarlo
                    price = syncPrice,
                    categoryColor = 0xFF00BCD4 // Color Cian para destacar
                )
                bookingDao.insertBooking(configBooking)
            }
        } catch (e: Exception) {
            // Si falla el fetch, Room mantendrá lo que ya tenía (Persistencia Offline)
        }
    }
}
