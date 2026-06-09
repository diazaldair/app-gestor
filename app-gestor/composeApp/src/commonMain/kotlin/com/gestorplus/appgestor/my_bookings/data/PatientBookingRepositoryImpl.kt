package com.gestorplus.appgestor.my_bookings.data

import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import com.gestorplus.appgestor.my_bookings.data.local.dao.PatientBookingDao
import com.gestorplus.appgestor.my_bookings.data.local.entity.PatientBookingEntity
import com.gestorplus.appgestor.my_bookings.domain.model.BookingStatus
import com.gestorplus.appgestor.my_bookings.domain.model.PatientBooking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class PatientBookingRepositoryImpl(
    private val firebaseManager: FirebaseManager,
    private val bookingDao: PatientBookingDao
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
            // En un escenario real, las reservas del paciente estarían en "users/$uid/bookings"
            val bookingsData = firebaseManager.getData("users/$uid/bookings") ?: return
            
            val bookingEntities = bookingsData.mapNotNull { (id, data) ->
                try {
                    val dataMap = data as? Map<String, Any> ?: return@mapNotNull null
                    PatientBookingEntity(
                        id = id,
                        clinicId = dataMap["clinicId"] as? String ?: "",
                        clinicName = dataMap["clinicName"] as? String ?: "",
                        serviceName = dataMap["serviceName"] as? String ?: "",
                        doctorName = dataMap["doctorName"] as? String ?: "Especialista",
                        doctorImageUrl = dataMap["doctorImageUrl"] as? String,
                        timestamp = (dataMap["timestamp"] as? Number)?.toLong() ?: 0L,
                        status = dataMap["status"] as? String ?: "PENDING",
                        price = (dataMap["price"] as? Number)?.toDouble() ?: 0.0,
                        currency = dataMap["currency"] as? String ?: "$"
                    )
                } catch (e: Exception) {
                    null
                }
            }
            
            if (bookingEntities.isNotEmpty()) {
                bookingDao.clearAllBookings()
                bookingDao.insertBookings(bookingEntities)
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
        currency = currency
    )
}
