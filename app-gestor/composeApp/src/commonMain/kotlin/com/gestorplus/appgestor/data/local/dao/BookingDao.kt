package com.gestorplus.appgestor.data.local.dao

import androidx.room.*
import com.gestorplus.appgestor.data.local.entity.BookingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY timestamp ASC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE timestamp >= :startDay AND timestamp <= :endDay ORDER BY timestamp ASC")
    fun getBookingsByDate(startDay: Long, endDay: Long): Flow<List<BookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Query("UPDATE bookings SET status = :newStatus WHERE id = :bookingId")
    suspend fun updateBookingStatus(bookingId: String, newStatus: String)

    @Delete
    suspend fun deleteBooking(booking: BookingEntity)
}
