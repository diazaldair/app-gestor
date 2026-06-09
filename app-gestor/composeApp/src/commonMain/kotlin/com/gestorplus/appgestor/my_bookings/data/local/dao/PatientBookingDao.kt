package com.gestorplus.appgestor.my_bookings.data.local.dao

import androidx.room.*
import com.gestorplus.appgestor.my_bookings.data.local.entity.PatientBookingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientBookingDao {
    @Query("SELECT * FROM patient_bookings ORDER BY timestamp DESC")
    fun getAllBookings(): Flow<List<PatientBookingEntity>>

    @Query("SELECT * FROM patient_bookings WHERE timestamp >= :now ORDER BY timestamp ASC")
    fun getUpcomingBookings(now: Long): Flow<List<PatientBookingEntity>>

    @Query("SELECT * FROM patient_bookings WHERE timestamp < :now ORDER BY timestamp DESC")
    fun getPastBookings(now: Long): Flow<List<PatientBookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookings(bookings: List<PatientBookingEntity>)

    @Query("DELETE FROM patient_bookings")
    suspend fun clearAllBookings()
}
