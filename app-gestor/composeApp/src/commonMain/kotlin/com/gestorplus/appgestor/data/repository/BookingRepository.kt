package com.gestorplus.appgestor.data.repository

import com.gestorplus.appgestor.data.local.dao.BookingDao
import com.gestorplus.appgestor.data.local.entity.BookingEntity
import com.gestorplus.appgestor.data.datasource.FirebaseManager
import kotlinx.coroutines.flow.Flow

class BookingRepository(
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
}
