package com.gestorplus.appgestor.booking.presentation.state

import androidx.compose.runtime.Immutable

@Immutable
data class BookingUiState(
    val clinicId: String = "",
    val serviceId: String = "",
    val clinicName: String = "",
    val serviceName: String = "",
    val selectedDate: Int = 1,
    val selectedMonth: String = "",
    val selectedDayOfWeek: String = "",
    val selectedTimeSlot: String? = null,
    val timeSlotsMorning: List<String> = emptyList(),
    val timeSlotsAfternoon: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
