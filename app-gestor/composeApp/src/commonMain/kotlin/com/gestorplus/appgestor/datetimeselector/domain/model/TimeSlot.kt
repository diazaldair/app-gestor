package com.gestorplus.appgestor.datetimeselector.domain.model

enum class TimePeriod {
    MORNING, AFTERNOON
}

data class TimeSlot(
    val id: String,
    val time: String,
    val isAvailable: Boolean = true,
    val period: TimePeriod
)
