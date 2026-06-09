package com.gestorplus.appgestor.owner.setup_schedule.data.local.dao

import androidx.room.*
import com.gestorplus.appgestor.owner.setup_schedule.data.local.entity.ShiftEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShiftDao {
    @Query("SELECT * FROM shifts")
    fun getAllShifts(): Flow<List<ShiftEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShifts(shifts: List<ShiftEntity>)

    @Query("DELETE FROM shifts")
    suspend fun deleteAllShifts()

    @Transaction
    suspend fun updateAllShifts(shifts: List<ShiftEntity>) {
        deleteAllShifts()
        insertShifts(shifts)
    }

    @Query("DELETE FROM shifts WHERE id = :shiftId")
    suspend fun deleteShiftById(shiftId: String)
}
