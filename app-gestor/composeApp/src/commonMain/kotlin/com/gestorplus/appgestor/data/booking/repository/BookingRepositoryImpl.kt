package com.gestorplus.appgestor.data.booking.repository

import com.gestorplus.appgestor.data.datasource.FirebaseManager
import com.gestorplus.appgestor.domain.booking.model.BookingSlot
import com.gestorplus.appgestor.domain.booking.model.SlotPeriod
import com.gestorplus.appgestor.domain.booking.repository.BookingRepository

class BookingRepositoryImpl(
    private val firebaseManager: FirebaseManager
) : BookingRepository {

    override suspend fun getAvailableSlots(date: Int): List<BookingSlot> {
        // En un caso real, consultaríamos Firebase.
        // Si no hay datos en Firebase para esa fecha, devolvemos los slots por defecto.
        val remoteSlots = firebaseManager.getData("available_slots/$date")
        
        if (remoteSlots != null) {
            return remoteSlots.map { (id, value) ->
                val parts = value.toString().split("|")
                BookingSlot(
                    id = id,
                    time = parts.getOrNull(0) ?: "00:00",
                    isAvailable = parts.getOrNull(1)?.toBoolean() ?: true,
                    period = SlotPeriod.valueOf(parts.getOrNull(2) ?: "MORNING")
                )
            }
        }

        // Mock data fallback si no hay nada en Firebase
        return listOf(
            BookingSlot("1", "09:00 AM", period = SlotPeriod.MORNING),
            BookingSlot("2", "09:30 AM", period = SlotPeriod.MORNING),
            BookingSlot("3", "10:00 AM", period = SlotPeriod.MORNING),
            BookingSlot("4", "10:30 AM", period = SlotPeriod.MORNING),
            BookingSlot("5", "11:00 AM", period = SlotPeriod.MORNING),
            BookingSlot("6", "11:30 AM", period = SlotPeriod.MORNING),
            BookingSlot("7", "01:00 PM", period = SlotPeriod.AFTERNOON),
            BookingSlot("8", "01:30 PM", period = SlotPeriod.AFTERNOON),
            BookingSlot("9", "02:00 PM", period = SlotPeriod.AFTERNOON),
            BookingSlot("10", "02:30 PM", period = SlotPeriod.AFTERNOON),
            BookingSlot("11", "03:00 PM", period = SlotPeriod.AFTERNOON),
            BookingSlot("12", "04:00 PM", period = SlotPeriod.AFTERNOON)
        )
    }

    override suspend fun confirmBooking(date: Int, slot: String): Result<Unit> {
        return try {
            // 1. Guardamos la reserva
            firebaseManager.saveData("bookings/$date/$slot", "CLIENT_ID_MOCK|CONFIRMED")
            // 2. Marcamos el slot como no disponible
            // firebaseManager.saveData("available_slots/$date/$slot/isAvailable", "false")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
