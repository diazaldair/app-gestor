package com.gestorplus.appgestor.owner.setup_schedule.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Shift(
    val id: String = "",
    val name: String = "",
    val startTime: String = "09:00 AM",
    val endTime: String = "05:00 PM",
    val days: List<String> = emptyList()
)
