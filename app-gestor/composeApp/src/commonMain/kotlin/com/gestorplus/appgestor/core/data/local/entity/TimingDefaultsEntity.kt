package com.gestorplus.appgestor.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timing_defaults")
data class TimingDefaultsEntity(
    @PrimaryKey val id: Int = 1,
    val durationMinutes: Int,
    val bufferMinutes: Int
)