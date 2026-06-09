package com.gestorplus.appgestor.core.data.local.dao

import androidx.room.*
import com.gestorplus.appgestor.core.data.local.entity.WorkingShiftEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkingShiftDao {
    @Query("SELECT * FROM working_shifts ORDER BY dayOfWeek")
    fun getAll(): Flow<List<WorkingShiftEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shift: WorkingShiftEntity)

    @Query("DELETE FROM working_shifts")
    suspend fun deleteAll()
}