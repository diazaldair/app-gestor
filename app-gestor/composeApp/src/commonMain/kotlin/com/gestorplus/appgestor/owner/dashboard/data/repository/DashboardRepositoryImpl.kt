package com.gestorplus.appgestor.owner.dashboard.data.repository

import com.gestorplus.appgestor.booking.data.datasource.dto.FirebaseBookingDto
import com.gestorplus.appgestor.core.util.DateTimeUtils
import com.gestorplus.appgestor.core.data.local.entity.BookingEntity
import com.gestorplus.appgestor.owner.dashboard.data.datasource.DashboardLocalDatasource
import com.gestorplus.appgestor.owner.dashboard.data.datasource.DashboardRemoteDatasource
import com.gestorplus.appgestor.owner.dashboard.data.mapper.DashboardMapper
import com.gestorplus.appgestor.owner.dashboard.domain.model.Booking
import com.gestorplus.appgestor.owner.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DashboardRepositoryImpl(
    private val localDatasource: DashboardLocalDatasource,
    private val remoteDatasource: DashboardRemoteDatasource,
    private val dashboardMapper: DashboardMapper
) : DashboardRepository {

    override fun getBookings(): Flow<List<Booking>> {
        return localDatasource.getBookingsFlow().map { entities ->
            entities.map { dashboardMapper.toDomain(it) }
        }
    }

    override suspend fun addBooking(booking: Booking) {
        val entity = dashboardMapper.toEntity(booking)
        localDatasource.saveBookings(listOf(entity))

        try {
            val dto = FirebaseBookingDto(
                clientName = booking.clientName,
                serviceName = booking.serviceName,
                status = booking.status
            )
            remoteDatasource.updateBooking(
                booking.id.split("-").first().toIntOrNull() ?: 0,
                booking.id.split("-").last(),
                dashboardMapper.toPipedString(dto)
            )
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
            remoteDatasource.saveData("bookings/$date/$slot/status", newStatus)
        } else {
            remoteDatasource.saveData("bookings/$bookingId/status", newStatus)
        }
    }

    override suspend fun syncAllBookings() {
        try {
            val bookingsRoot = remoteDatasource.getData("bookings") ?: return

            (bookingsRoot as? Map<String, Any>)?.forEach { (date, slots) ->
                (slots as? Map<String, String>)?.forEach { (slotId, value) ->
                    val dto = dashboardMapper.parseBooking(value)
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

    override suspend fun getFirebaseLogs(path: String): List<String> {
        return remoteDatasource.getFirebaseLogs(path)
    }
}
