package com.gestorplus.appgestor.owner.schedule.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TimingDefaults(
    val defaultDurationMinutes: Int = 60,
    val defaultBufferMinutes: Int = 15
)