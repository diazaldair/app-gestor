package com.gestorplus.appgestor.booking.presentation.state

import androidx.compose.runtime.Immutable

@Immutable
data class BookingState(
    val selectedDate: Int = 5,
    val selectedMonth: String = "October 2023",
    val selectedTimeSlot: String? = null,
    val timeSlotsMorning: List<String> = emptyList(),
    val timeSlotsAfternoon: List<String> = emptyList(),
    val isLoading: Boolean = false
)

sealed interface BookingEvent {
    data class OnDateSelected(val date: Int) : BookingEvent
    data class OnTimeSlotSelected(val slot: String) : BookingEvent
    data object OnConfirmBooking : BookingEvent
    data object OnBackClicked : BookingEvent
}

sealed interface BookingEffect {
    data object BookingConfirmed : BookingEffect
    data object NavigateBack : BookingEffect
    data class ShowError(val message: String) : BookingEffect
}
