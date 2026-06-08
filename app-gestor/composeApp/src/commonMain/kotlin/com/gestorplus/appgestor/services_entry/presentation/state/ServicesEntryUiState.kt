package com.gestorplus.appgestor.services_entry.presentation.state

import androidx.compose.runtime.Immutable

@Immutable
data class ServicesEntryUiState(
    val professionalName: String = "Dr. Castro",
    val appointmentsCount: Int = 8,
    val pendingCount: Int = 3,
    val isLoading: Boolean = false
)
