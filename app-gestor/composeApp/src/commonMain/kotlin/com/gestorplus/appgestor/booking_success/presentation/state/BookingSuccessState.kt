package com.gestorplus.appgestor.booking_success.presentation.state

import com.gestorplus.appgestor.booking_success.domain.model.BookingSuccessDetails

data class BookingSuccessState(
    val details: BookingSuccessDetails? = null,
    val isLoading: Boolean = false
)
