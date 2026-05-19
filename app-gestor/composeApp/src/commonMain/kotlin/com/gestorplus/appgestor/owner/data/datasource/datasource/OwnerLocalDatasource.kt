package com.gestorplus.appgestor.owner.data.datasource.datasource

import com.gestorplus.appgestor.data.local.dao.BookingDao
import com.gestorplus.appgestor.data.local.entity.BookingEntity
import kotlinx.coroutines.flow.Flow

class OwnerLocalDatasource(private val bookingDao: BookingDao) {
    fun getBookingsFlow(): Flow<List<BookingEntity>> {
        return bookingDao.getAllBookings()
    }

    suspend fun saveBookings(entities: List<BookingEntity>) {
        entities.forEach { bookingDao.insertBooking(it) }
    }

    suspend fun updateBookingStatus(id: String, status: String) {
        bookingDao.updateBookingStatus(id, status)
    }
}
