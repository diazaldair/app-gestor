package com.gestorplus.appgestor.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "working_shifts")
data class WorkingShiftEntity(
    @PrimaryKey val dayOfWeek: Int,
    val morningStart: String?,
    val morningEnd: String?,
    val afternoonStart: String?,
    val afternoonEnd: String?
)