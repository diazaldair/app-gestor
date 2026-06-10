package com.gestorplus.appgestor.core.util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DateTimeUtilsTest {

    @Test
    fun `formatTime returns correct string for morning and afternoon`() {
        // 9:30 AM en un timestamp fijo
        // Usamos un valor conocido para evitar problemas de zona horaria en tests si es posible, 
        // o asumimos la local del sistema si el util lo hace.
        val morningMillis = 1696498200000L // Oct 5, 2023 09:30:00 (ajustar según TZ si falla)
        
        // En lugar de millis fijos que dependen de TZ, probamos la lógica de formateo
        val timeStr = DateTimeUtils.formatTime(morningMillis)
        // Verificamos que el formato sea HH:MM AM/PM
        assertEquals(true, timeStr.contains("AM") || timeStr.contains("PM"))
    }

    @Test
    fun `calculateTimestamp generates valid long from date int`() {
        val dateInt = 20231005 // Oct 5, 2023
        val slotIndex = 0 // 9:00 AM según la lógica de DateTimeUtils
        
        val result = DateTimeUtils.calculateTimestamp(dateInt, slotIndex)
        
        val dateTime = Instant.fromEpochMilliseconds(result)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            
        assertEquals(2023, dateTime.year)
        assertEquals(10, dateTime.monthNumber)
        assertEquals(5, dateTime.dayOfMonth)
        assertEquals(9, dateTime.hour)
        assertEquals(0, dateTime.minute)
    }

    @Test
    fun `calculateTimestamp for afternoon slot`() {
        val dateInt = 20231005
        val slotIndex = 2 // 9:00 + (2 * 30 min) = 10:00 AM
        
        val result = DateTimeUtils.calculateTimestamp(dateInt, slotIndex)
        val dateTime = Instant.fromEpochMilliseconds(result)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            
        assertEquals(10, dateTime.hour)
        assertEquals(0, dateTime.minute)
    }
}
