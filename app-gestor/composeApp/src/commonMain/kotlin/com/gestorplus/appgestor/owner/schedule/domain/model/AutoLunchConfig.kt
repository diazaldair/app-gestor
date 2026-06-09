package com.gestorplus.appgestor.owner.schedule.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AutoLunchConfig(
    val enabled: Boolean = true,
    val startTime: String = "12:00 PM",
    val endTime: String = "01:00 PM"
)