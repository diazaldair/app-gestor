package com.gestorplus.appgestor.owner.dashboard.presentation.state

sealed interface OwnerDashboardEvent {
    data class OnAcceptBooking(val bookingId: String) : OwnerDashboardEvent
    data class OnRejectBooking(val bookingId: String) : OwnerDashboardEvent
    data class OnDateSelected(val day: Int) : OwnerDashboardEvent
    data class OnMonthChange(val increment: Int) : OwnerDashboardEvent
    data class OnFilterChanged(val filter: BookingFilter) : OwnerDashboardEvent
    data object OnLoadLogs : OwnerDashboardEvent
    data object OnRefreshBookings : OwnerDashboardEvent
    data object OnClearError : OwnerDashboardEvent
}
