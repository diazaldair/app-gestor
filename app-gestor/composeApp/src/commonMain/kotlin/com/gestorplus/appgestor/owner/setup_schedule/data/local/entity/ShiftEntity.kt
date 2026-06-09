package com.gestorplus.appgestor.owner.setup_schedule.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shifts")
data class ShiftEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val startTime: String,
    val endTime: String,
    val daysJson: String // Guardamos los días como JSON string ["L", "M"...]
)
