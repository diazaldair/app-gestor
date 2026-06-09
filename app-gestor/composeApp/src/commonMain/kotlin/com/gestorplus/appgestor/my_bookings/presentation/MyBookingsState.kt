package com.gestorplus.appgestor.my_bookings.presentation

import com.gestorplus.appgestor.my_bookings.domain.model.PatientBooking

data class MyBookingsState(
    val upcomingBookings: List<PatientBooking> = emptyList(),
    val pastBookings: List<PatientBooking> = emptyList(),
    val selectedTab: BookingTab = BookingTab.UPCOMING,
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class BookingTab {
    UPCOMING, PAST
}
