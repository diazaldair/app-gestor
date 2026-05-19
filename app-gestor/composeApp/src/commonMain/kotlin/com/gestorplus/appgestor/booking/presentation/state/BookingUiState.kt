package com.gestorplus.appgestor.booking.presentation.state

import androidx.compose.runtime.Immutable

@Immutable
data class BookingUiState(
    val selectedDate: Int = 5,
    val selectedMonth: String = "October 2023",
    val selectedTimeSlot: String? = null,
    val timeSlotsMorning: List<String> = emptyList(),
    val timeSlotsAfternoon: List<String> = emptyList(),
    val isLoading: Boolean = false
)
