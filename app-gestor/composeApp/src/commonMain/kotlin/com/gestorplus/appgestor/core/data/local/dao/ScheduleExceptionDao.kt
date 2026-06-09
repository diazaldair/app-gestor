package com.gestorplus.appgestor.core.data.local.dao

import androidx.room.*
import com.gestorplus.appgestor.core.data.local.entity.ScheduleExceptionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleExceptionDao {
    @Query("SELECT * FROM schedule_exceptions ORDER BY date")
    fun getAll(): Flow<List<ScheduleExceptionEntity>>

    @Query("SELECT * FROM schedule_exceptions WHERE date = :date")
    suspend fun getByDate(date: String): ScheduleExceptionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exception: ScheduleExceptionEntity)

    @Delete
    suspend fun delete(exception: ScheduleExceptionEntity)
}