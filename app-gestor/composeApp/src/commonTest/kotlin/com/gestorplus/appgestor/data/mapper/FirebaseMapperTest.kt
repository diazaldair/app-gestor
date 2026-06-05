package com.gestorplus.appgestor.booking.data.mapper

import com.gestorplus.appgestor.booking.data.datasource.dto.FirebaseBookingDto
import com.gestorplus.appgestor.booking.domain.model.SlotPeriod
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FirebaseMapperTest {

    private val mapper = FirebaseMapper()

    @Test
    fun `parseBooking with valid data should return correct DTO`() {
        val rawValue = "John Doe|Barber Service|CONFIRMED"
        val result = mapper.parseBooking(rawValue)

        assertEquals("John Doe", result.clientName)
        assertEquals("Barber Service", result.serviceName)
        assertEquals("CONFIRMED", result.status)
    }

    @Test
    fun `parseBooking with missing fields should use default values`() {
        val rawValue = "John Doe"
        val result = mapper.parseBooking(rawValue)

        assertEquals("John Doe", result.clientName)
        assertEquals("General Service", result.serviceName)
        assertEquals("PENDING", result.status)
    }

    @Test
    fun `parseBooking with empty string should return default DTO`() {
        val rawValue = ""
        val result = mapper.parseBooking(rawValue)

        assertEquals("Unknown", result.clientName)
        assertEquals("General Service", result.serviceName)
        assertEquals("PENDING", result.status)
    }

    @Test
    fun `parseSlot with valid data should return correct DTO`() {
        val rawValue = "10:00 AM|true|MORNING"
        val result = mapper.parseSlot(rawValue)

        assertEquals("10:00 AM", result.time)
        assertTrue(result.isAvailable)
        assertEquals(SlotPeriod.MORNING, result.period)
    }

    @Test
    fun `parseSlot with invalid period should fallback to MORNING`() {
        val rawValue = "02:00 PM|true|INVALID_PERIOD"
        val result = mapper.parseSlot(rawValue)

        assertEquals("02:00 PM", result.time)
        assertEquals(SlotPeriod.MORNING, result.period)
    }

    @Test
    fun `parseSlot with missing fields should use defaults`() {
        val rawValue = "03:00 PM"
        val result = mapper.parseSlot(rawValue)

        assertEquals("03:00 PM", result.time)
        assertTrue(result.isAvailable)
        assertEquals(SlotPeriod.MORNING, result.period)
    }

    @Test
    fun `toPipedString should format DTO correctly`() {
        val dto = FirebaseBookingDto(
            clientName = "Alice",
            serviceName = "Consulting",
            status = "PAID"
        )
        val result = mapper.toPipedString(dto)

        assertEquals("Alice|Consulting|PAID", result)
    }

    @Test
    fun `round trip test for booking data`() {
        val originalDto = FirebaseBookingDto(
            clientName = "Bob",
            serviceName = "Massage",
            status = "CONFIRMED"
        )
        
        val piped = mapper.toPipedString(originalDto)
        val parsed = mapper.parseBooking(piped)

        assertEquals(originalDto.clientName, parsed.clientName)
        assertEquals(originalDto.serviceName, parsed.serviceName)
        assertEquals(originalDto.status, parsed.status)
    }
}
