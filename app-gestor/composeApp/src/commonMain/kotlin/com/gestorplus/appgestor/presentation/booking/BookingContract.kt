package com.gestorplus.appgestor.presentation.booking

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
    object OnConfirmBooking : BookingEvent
    object OnBackClicked : BookingEvent
}

sealed interface BookingEffect {
    object BookingConfirmed : BookingEffect
    object NavigateBack : BookingEffect
    data class ShowError(val message: String) : BookingEffect
}
