package com.gestorplus.appgestor.owner.presentation.state

import androidx.compose.runtime.Immutable
import com.gestorplus.appgestor.owner.domain.model.Booking

@Immutable
data class OwnerDashboardUiState(
    val bookings: List<Booking> = emptyList(),
    val firebaseLogs: List<String> = emptyList(),
    val isLoadingLogs: Boolean = false,
    val isSyncing: Boolean = false,
    val error: String? = null
)
