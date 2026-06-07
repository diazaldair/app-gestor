package com.gestorplus.appgestor.booking.presentation.state

sealed interface BookingEvent {
    data class OnDateSelected(val date: Int) : BookingEvent
    data class OnTimeSlotSelected(val slot: String) : BookingEvent
    data class OnNotesChanged(val notes: String) : BookingEvent
    data object OnConfirmBooking : BookingEvent
    data object OnBackClicked : BookingEvent
}
