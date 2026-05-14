package com.gestorplus.appgestor.core.util

import kotlinx.datetime.*

object DateTimeUtils {
    fun formatTime(timestamp: Long): String {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        
        val hour = dateTime.hour
        val minute = dateTime.minute
        val amPm = if (hour < 12) "AM" else "PM"
        val displayHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        
        return "${displayHour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')} $amPm"
    }

    // Helper to calculate timestamp from date string (YYYYMMDD) and slot index
    fun calculateTimestamp(dateInt: Int, slotIndex: Int): Long {
        val dateStr = dateInt.toString()
        if (dateStr.length != 8) return System.currentTimeMillis()
        
        val year = dateStr.substring(0, 4).toInt()
        val month = dateStr.substring(4, 6).toInt()
        val day = dateStr.substring(6, 8).toInt()
        
        // Assume slots start at 9:00 AM, each slot is 30 mins
        val startHour = 9
        val totalMinutes = slotIndex * 30
        val finalHour = startHour + (totalMinutes / 60)
        val finalMinute = totalMinutes % 60
        
        return LocalDateTime(year, month, day, finalHour, finalMinute)
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
    }
}
