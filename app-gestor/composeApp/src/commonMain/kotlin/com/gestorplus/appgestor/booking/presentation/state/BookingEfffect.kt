package com.gestorplus.appgestor.booking.presentation.state

sealed interface BookingEfffect {
    data object BookingConfirmed : BookingEfffect
    data object NavigateBack : BookingEfffect
    data class ShowError(val message: String) : BookingEfffect
}
