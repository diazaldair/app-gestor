package com.gestorplus.appgestor.core.data.local.dao

import androidx.room.*
import com.gestorplus.appgestor.core.data.local.entity.AutoLunchEntity

@Dao
interface AutoLunchDao {
    @Query("SELECT * FROM auto_lunch WHERE id = 1")
    suspend fun get(): AutoLunchEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AutoLunchEntity)
}