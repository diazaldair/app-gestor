package com.gestorplus.appgestor.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule_exceptions")
data class ScheduleExceptionEntity(
    @PrimaryKey val id: String,
    val date: String,
    val isOpen: Boolean,
    val customMorningStart: String?,
    val customMorningEnd: String?,
    val customAfternoonStart: String?,
    val customAfternoonEnd: String?,
    val reason: String?
)