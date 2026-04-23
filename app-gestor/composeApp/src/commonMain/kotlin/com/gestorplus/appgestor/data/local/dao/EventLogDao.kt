package com.gestorplus.appgestor.data.local.dao

import androidx.room.*
import com.gestorplus.appgestor.data.local.entity.EventLogEntity

@Dao
interface EventLogDao {
    @Insert
    suspend fun insertEvent(event: EventLogEntity)

    @Query("SELECT * FROM event_logs WHERE synced = 0")
    suspend fun getUnsyncedEvents(): List<EventLogEntity>

    @Query("UPDATE event_logs SET synced = 1 WHERE id = :eventId")
    suspend fun markAsSynced(eventId: Int)
}
