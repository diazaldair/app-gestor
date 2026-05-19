package com.gestorplus.appgestor.owner.presentation.state

import androidx.compose.runtime.Immutable
import com.gestorplus.appgestor.owner.domain.model.Booking

@Immutable
data class OwnerDashboardState(
    val bookings: List<Booking> = emptyList(),
    val firebaseLogs: List<String> = emptyList(),
    val isLoadingLogs: Boolean = false,
    val isSyncing: Boolean = false,
    val error: String? = null
)

sealed interface OwnerDashboardEvent {
    data class OnAcceptBooking(val bookingId: String) : OwnerDashboardEvent
    data class OnRejectBooking(val bookingId: String) : OwnerDashboardEvent
    data class OnDateSelected(val day: Int) : OwnerDashboardEvent
    data class OnMonthChange(val increment: Int) : OwnerDashboardEvent
    data object OnLoadLogs : OwnerDashboardEvent
    data object OnRefreshBookings : OwnerDashboardEvent
    data object OnClearError : OwnerDashboardEvent
}

sealed interface OwnerDashboardEffect {
    data class ShowSnackbar(val message: String) : OwnerDashboardEffect
}
