package com.gestorplus.appgestor.core.data.local.dao

import androidx.room.*
import com.gestorplus.appgestor.core.data.local.entity.MasterScheduleEntity

@Dao
interface MasterScheduleDao {
    @Query("SELECT * FROM master_schedule WHERE id = 1")
    suspend fun get(): MasterScheduleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: MasterScheduleEntity)
}