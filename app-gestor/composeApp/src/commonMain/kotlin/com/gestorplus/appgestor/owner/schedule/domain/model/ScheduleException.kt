package com.gestorplus.appgestor.owner.schedule.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleException(
    val id: String = "",
    val date: String, // yyyy-MM-dd
    val isOpen: Boolean, // true = abierto, false = bloqueado
    val customMorningStart: String? = null,
    val customMorningEnd: String? = null,
    val customAfternoonStart: String? = null,
    val customAfternoonEnd: String? = null,
    val reason: String? = null
)