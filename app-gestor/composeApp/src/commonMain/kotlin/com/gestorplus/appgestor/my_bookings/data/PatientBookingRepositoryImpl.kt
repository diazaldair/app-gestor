package com.gestorplus.appgestor.my_bookings.data

import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import com.gestorplus.appgestor.my_bookings.data.local.dao.PatientBookingDao
import com.gestorplus.appgestor.my_bookings.data.local.entity.PatientBookingEntity
import com.gestorplus.appgestor.my_bookings.domain.model.BookingStatus
import com.gestorplus.appgestor.my_bookings.domain.model.PatientBooking
import com.gestorplus.appgestor.notifications.data.local.dao.NotificationDao
import com.gestorplus.appgestor.notifications.data.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class PatientBookingRepositoryImpl(
    private val firebaseManager: FirebaseManager,
    private val bookingDao: PatientBookingDao,
    private val notificationDao: NotificationDao
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun getUpcomingBookings(): Flow<List<PatientBooking>> {
        val now = System.currentTimeMillis()
        return bookingDao.getUpcomingBookings(now).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getPastBookings(): Flow<List<PatientBooking>> {
        val now = System.currentTimeMillis()
        return bookingDao.getPastBookings(now).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun syncBookings() {
        try {
            val uid = firebaseManager.getCurrentUserUid() ?: return
            val bookingsData = firebaseManager.getData("users/$uid/bookings") ?: return
            
            val currentBookings = bookingDao.getAllBookingsSync()
            val newNotifications = mutableListOf<NotificationEntity>()

            val bookingEntities = bookingsData.mapNotNull { (bookingId, data) ->
                try {
                    val dataMap = data as? Map<String, Any> ?: return@mapNotNull null
                    val statusStr = dataMap["status"] as? String ?: "PENDING"
                    
                    // Check if status changed to CONFIRMED for notifications
                    val oldBooking = currentBookings.find { it.id == bookingId }
                    if (oldBooking != null && oldBooking.status != "CONFIRMED" && statusStr == "CONFIRMED") {
                        newNotifications.add(
                            NotificationEntity(
                                id = "notif_${bookingId}_${System.currentTimeMillis()}",
                                title = "¡Reserva Confirmada!",
                                description = "Tu cita con ${dataMap["doctorName"]} ha sido confirmada.",
                                timestamp = System.currentTimeMillis(),
                                type = "CONFIRMATION",
                                patientName = null,
                                specialty = dataMap["serviceName"] as? String,
                                appointmentDate = "${dataMap["date"]} ${dataMap["month"]}",
                                appointmentTime = dataMap["timeSlot"] as? String,
                                isRead = false
                            )
                        )
                    }

                    PatientBookingEntity(
                        id = bookingId,
                        clinicId = dataMap["clinicId"] as? String ?: "",
                        clinicName = dataMap["clinicName"] as? String ?: "",
                        serviceName = dataMap["serviceName"] as? String ?: "",
                        doctorName = dataMap["doctorName"] as? String ?: "Especialista",
                        doctorImageUrl = dataMap["doctorImageUrl"] as? String,
                        timestamp = (dataMap["timestamp"] as? Number)?.toLong() ?: 0L,
                        status = statusStr,
                        price = (dataMap["price"] as? Number)?.toDouble() ?: 0.0,
                        currency = dataMap["currency"] as? String ?: "$",
                        date = (dataMap["date"] as? Number)?.toInt(),
                        month = dataMap["month"] as? String,
                        timeSlot = dataMap["timeSlot"] as? String,
                        notes = dataMap["notes"] as? String
                    )
                } catch (e: Exception) {
                    null
                }
            }
            
            if (bookingEntities.isNotEmpty()) {
                bookingDao.clearAllBookings()
                bookingDao.insertBookings(bookingEntities)
            }
            
            if (newNotifications.isNotEmpty()) {
                notificationDao.insertNotifications(newNotifications)
            }
        } catch (e: Exception) {
            // Log error
        }
    }

    private fun PatientBookingEntity.toDomain() = PatientBooking(
        id = id,
        clinicId = clinicId,
        clinicName = clinicName,
        serviceName = serviceName,
        doctorName = doctorName,
        doctorImageUrl = doctorImageUrl,
        timestamp = timestamp,
        status = try { BookingStatus.valueOf(status) } catch (e: Exception) { BookingStatus.PENDING },
        price = price,
        currency = currency,
        date = date,
        month = month,
        timeSlot = timeSlot,
        notes = notes
    )
}
