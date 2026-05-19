package com.gestorplus.appgestor.datetimeselector.presentation.state

sealed interface DateTimeSelectorEvent {
    data class OnDateSelected(val date: Int) : DateTimeSelectorEvent
    data class OnTimeSlotSelected(val slotId: String) : DateTimeSelectorEvent
    data object OnConfirmClicked : DateTimeSelectorEvent
    data object OnBackClicked : DateTimeSelectorEvent
}
