package com.gestorplus.appgestor.datetimeselector.presentation.state

sealed interface DateTimeSelectorEfffect {
    data object NavigateBack : DateTimeSelectorEfffect
    data class BookingConfirmed(val date: Int, val time: String) : DateTimeSelectorEfffect
}
