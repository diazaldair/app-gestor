package com.gestorplus.appgestor.owner.dashboard.data.repository

import com.gestorplus.appgestor.core.util.DateTimeUtils
import com.gestorplus.appgestor.core.data.local.entity.BookingEntity
import com.gestorplus.appgestor.owner.dashboard.data.datasource.DashboardLocalDatasource
import com.gestorplus.appgestor.owner.dashboard.data.datasource.DashboardRemoteDatasource
import com.gestorplus.appgestor.owner.dashboard.data.mapper.DashboardMapper
import com.gestorplus.appgestor.owner.dashboard.domain.model.Booking
import com.gestorplus.appgestor.owner.dashboard.domain.repository.DashboardRepository
import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class DashboardRepositoryImpl(
    private val localDatasource: DashboardLocalDatasource,
    private val remoteDatasource: DashboardRemoteDatasource,
    private val dashboardMapper: DashboardMapper,
    private val firebaseManager: FirebaseManager
) : DashboardRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override fun getBookings(): Flow<List<Booking>> {
        return localDatasource.getBookingsFlow().map { entities ->
            entities.map { dashboardMapper.toDomain(it) }
        }
    }

    override suspend fun addBooking(booking: Booking) {
        // Implementation for adding manually from dashboard if needed
    }

    override suspend fun updateStatus(bookingId: String, newStatus: String) {
        localDatasource.updateBookingStatus(bookingId, newStatus)
        val uid = firebaseManager.getCurrentUserUid() ?: return
        firebaseManager.saveData("workspaces/$uid/appointments/$bookingId/status", newStatus)
    }

    override suspend fun syncAllBookings() {
        try {
            val uid = firebaseManager.getCurrentUserUid() ?: return
            val appointmentsData = firebaseManager.getData("workspaces/$uid/appointments") ?: return

            val bookingsList = (appointmentsData as? Map<String, Any>)?.mapNotNull { (id, data) ->
                try {
                    val jsonString = data as? String ?: return@mapNotNull null
                    val dataMap = json.decodeFromString<Map<String, String>>(jsonString)
                    
                    // Solo mostramos las aceptadas en el Dashboard principal/calendario
                    if (dataMap["status"] != "ACCEPTED") return@mapNotNull null

                    BookingEntity(
                        id = id,
                        clientName = dataMap["patientName"] ?: "Paciente",
                        serviceName = dataMap["serviceName"] ?: "Servicio",
                        timestamp = dataMap["timestamp"]?.toLongOrNull() ?: 0L,
                        durationMinutes = 30, // TODO: Get from service
                        status = "ACCEPTED",
                        price = dataMap["price"]?.toDoubleOrNull() ?: 0.0,
                        categoryColor = 0xFF3B82F6
                    )
                } catch (e: Exception) {
                    null
                }
            } ?: emptyList()

            if (bookingsList.isNotEmpty()) {
                localDatasource.saveBookings(bookingsList)
            }
        } catch (e: Exception) {
            println("DEBUG: Error syncAllBookings: ${e.message}")
        }
    }

    override suspend fun getFirebaseLogs(path: String): List<String> {
        return remoteDatasource.getFirebaseLogs(path)
    }
}
