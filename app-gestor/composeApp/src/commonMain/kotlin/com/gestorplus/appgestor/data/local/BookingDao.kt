package com.gestorplus.appgestor.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Query("SELECT * FROM bookings ORDER BY date DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("DELETE FROM bookings WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("SELECT * FROM bookings WHERE id = :id")
    suspend fun getBookingById(id: Long): BookingEntity?
}
