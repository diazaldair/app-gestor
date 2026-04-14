package com.gestorplus.appgestor.domain.booking.model

data class BookingSlot(
    val id: String,
    val time: String,
    val isAvailable: Boolean = true,
    val period: SlotPeriod
)

enum class SlotPeriod {
    MORNING, AFTERNOON
}
