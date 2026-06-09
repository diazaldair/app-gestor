package com.gestorplus.appgestor.booking.presentation.state

sealed interface BookingConfirmationEffect {
    data object NavigateBack : BookingConfirmationEffect
    data object NavigateToHome : BookingConfirmationEffect
}
