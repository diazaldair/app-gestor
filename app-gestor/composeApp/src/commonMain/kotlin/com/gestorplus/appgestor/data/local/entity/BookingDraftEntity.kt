package com.gestorplus.appgestor.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "booking_drafts")
data class BookingDraftEntity(
    @PrimaryKey val id: Int = 1,
    val selectedDate: Int,
    val selectedMonth: String,
    val selectedTimeSlot: String?,
    val lastUpdated: Long = System.currentTimeMillis()
)
