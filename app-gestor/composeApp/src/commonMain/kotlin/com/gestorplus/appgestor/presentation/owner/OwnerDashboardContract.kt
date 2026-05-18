package com.gestorplus.appgestor.presentation.owner

import androidx.compose.runtime.Immutable
import com.gestorplus.appgestor.data.local.entity.BookingEntity

@Immutable
data class OwnerDashboardState(
    val bookings: List<BookingEntity> = emptyList(),
    val firebaseLogs: List<String> = emptyList(),
    val isLoadingLogs: Boolean = false,
    val isSyncing: Boolean = false,
    val error: String? = null
)

sealed interface OwnerDashboardEvent {
    data object OnRefreshBookings : OwnerDashboardEvent
    data class OnAcceptBooking(val bookingId: String) : OwnerDashboardEvent
    data class OnRejectBooking(val bookingId: String) : OwnerDashboardEvent
    data object OnLoadLogs : OwnerDashboardEvent
    data object OnClearError : OwnerDashboardEvent
}

sealed interface OwnerDashboardEffect {
    data class ShowSnackbar(val message: String) : OwnerDashboardEffect
}
