package com.gestorplus.appgestor.booking.data.datasource.repository

import com.gestorplus.appgestor.booking.data.datasource.datasource.BookingRemoteDatasource
import com.gestorplus.appgestor.booking.data.datasource.mapper.BookingMapper
import com.gestorplus.appgestor.booking.domain.model.BookingSlot
import com.gestorplus.appgestor.booking.domain.model.SlotPeriod
import com.gestorplus.appgestor.booking.domain.repository.BookingRepository
import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class BookingRepositoryImpl(
    private val bookingRemoteDatasource: BookingRemoteDatasource,
    private val bookingMapper: BookingMapper,
    private val firebaseManager: FirebaseManager
) : BookingRepository {

    override suspend fun getAvailableSlots(date: Int): List<BookingSlot> {
        val remoteSlots = bookingRemoteDatasource.getAvailableSlots(date)
        
        if (remoteSlots != null) {
            return remoteSlots.map { (id, value) ->
                val dto = bookingMapper.parseSlot(value.toString())
                bookingMapper.toDomain(id, dto)
            }
        }

        return getDefaultSlots()
    }

    override suspend fun confirmBooking(
        clinicId: String,
        serviceId: String,
        clinicName: String,
        serviceName: String,
        price: Double,
        date: Int,
        month: String,
        timeSlot: String
    ): Result<Unit> {
        return try {
            val uid = firebaseManager.getCurrentUserUid() ?: throw Exception("Usuario no autenticado")
            val now = Clock.System.now().toEpochMilliseconds()
            
            // Datos de la cita
            val bookingData = mapOf(
                "id" to now.toString(),
                "clinicId" to clinicId,
                "serviceId" to serviceId,
                "clinicName" to clinicName,
                "serviceName" to serviceName,
                "doctorName" to clinicName,
                "price" to price.toString(),
                "date" to date.toString(),
                "month" to month,
                "timeSlot" to timeSlot,
                "status" to "PENDING",
                "timestamp" to now.toString(),
                "patientId" to uid
            )

            val bookingJson = Json.encodeToString(bookingData)
            
            // Solo guardamos en Firebase. La Cloud Function detectará este cambio y enviará el PUSH.
            firebaseManager.saveData("users/$uid/bookings/$now", bookingJson)
            firebaseManager.saveData("workspaces/$clinicId/appointments/$now", bookingJson)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getDefaultSlots() = listOf(
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
