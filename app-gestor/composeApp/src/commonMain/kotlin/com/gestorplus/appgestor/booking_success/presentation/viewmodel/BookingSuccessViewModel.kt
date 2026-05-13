package com.gestorplus.appgestor.booking_success.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.gestorplus.appgestor.booking_success.domain.model.BookingSuccessDetails
import com.gestorplus.appgestor.booking_success.presentation.state.BookingSuccessState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BookingSuccessViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        BookingSuccessState(
            details = BookingSuccessDetails(
                serviceName = "Professional Consultation",
                schedule = "Tomorrow at 10:30 AM"
            )
        )
    )
    val uiState = _uiState.asStateFlow()
}
