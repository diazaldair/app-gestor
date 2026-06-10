package com.gestorplus.appgestor.my_bookings.data

import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import com.gestorplus.appgestor.my_bookings.data.local.dao.PatientBookingDao
import com.gestorplus.appgestor.my_bookings.data.local.entity.PatientBookingEntity
import com.gestorplus.appgestor.my_bookings.domain.model.BookingStatus
import com.gestorplus.appgestor.my_bookings.domain.model.PatientBooking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class PatientBookingRepositoryImpl(
    private val firebaseManager: FirebaseManager,
    private val bookingDao: PatientBookingDao
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun getUpcomingBookings(): Flow<List<PatientBooking>> {
        val now = ClockSystemNow()
        return bookingDao.getUpcomingBookings(now).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getPastBookings(): Flow<List<PatientBooking>> {
        val now = ClockSystemNow()
        return bookingDao.getPastBookings(now).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    private fun ClockSystemNow(): Long = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()

    suspend fun syncBookings() {
        try {
            val uid = firebaseManager.getCurrentUserUid() ?: return
            val bookingsData = firebaseManager.getData("users/$uid/bookings") ?: return
            
            val bookingEntities = bookingsData.mapNotNull { (id, data) ->
                try {
                    // El dato viene como un String JSON según BookingRepositoryImpl
                    val jsonString = data as? String ?: return@mapNotNull null
                    val dataMap = json.decodeFromString<Map<String, String>>(jsonString)
                    
                    PatientBookingEntity(
                        id = id,
                        clinicId = dataMap["clinicId"] ?: "",
                        clinicName = dataMap["clinicName"] ?: "",
                        serviceName = dataMap["serviceName"] ?: "",
                        doctorName = dataMap["doctorName"] ?: "Especialista",
                        doctorImageUrl = dataMap["doctorImageUrl"],
                        timestamp = dataMap["timestamp"]?.toLongOrNull() ?: 0L,
                        status = dataMap["status"] ?: "PENDING",
                        price = dataMap["price"]?.toDoubleOrNull() ?: 0.0,
                        currency = dataMap["currency"] ?: "$"
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
            println("DEBUG: Error syncBookings: ${e.message}")
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
