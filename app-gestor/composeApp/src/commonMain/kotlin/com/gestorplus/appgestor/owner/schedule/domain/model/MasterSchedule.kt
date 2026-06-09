package com.gestorplus.appgestor.owner.schedule.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MasterSchedule(
    val startTime: String, // "09:00 AM"
    val endTime: String,   // "05:00 PM"
    val enabledDays: List<Int> // días de la semana a los que aplica (1=Lun..7=Dom)
)