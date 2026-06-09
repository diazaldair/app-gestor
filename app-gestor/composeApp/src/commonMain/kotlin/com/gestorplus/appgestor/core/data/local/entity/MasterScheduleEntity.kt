package com.gestorplus.appgestor.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "master_schedule")
data class MasterScheduleEntity(
    @PrimaryKey val id: Int = 1,
    val startTime: String,
    val endTime: String,
    val enabledDays: String // almacenar como "1,2,3,4,5"
)