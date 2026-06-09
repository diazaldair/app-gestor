package com.gestorplus.appgestor.core.data.local.dao

import androidx.room.*
import com.gestorplus.appgestor.core.data.local.entity.TimingDefaultsEntity

@Dao
interface TimingDefaultsDao {
    @Query("SELECT * FROM timing_defaults WHERE id = 1")
    suspend fun get(): TimingDefaultsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TimingDefaultsEntity)
}