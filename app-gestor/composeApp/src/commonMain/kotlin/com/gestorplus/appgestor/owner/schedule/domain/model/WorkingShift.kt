package com.gestorplus.appgestor.owner.schedule.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkingShift(
    val dayOfWeek: Int, // 1 = Monday ... 7 = Sunday
    val morningStart: String? = null,
    val morningEnd: String? = null,
    val afternoonStart: String? = null,
    val afternoonEnd: String? = null
)