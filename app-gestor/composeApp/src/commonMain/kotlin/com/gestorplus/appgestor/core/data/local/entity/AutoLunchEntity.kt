package com.gestorplus.appgestor.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auto_lunch")
data class AutoLunchEntity(
    @PrimaryKey val id: Int = 1,
    val enabled: Boolean,
    val startTime: String,
    val endTime: String
)