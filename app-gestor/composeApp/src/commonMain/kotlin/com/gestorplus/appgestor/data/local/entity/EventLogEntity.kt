package com.gestorplus.appgestor.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_logs")
data class EventLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventType: String, // APP_OPEN, APP_CLOSE
    val timestamp: Long = System.currentTimeMillis(),
    val synced: Boolean = false // Para saber si ya se subió a Realtime Database
)
