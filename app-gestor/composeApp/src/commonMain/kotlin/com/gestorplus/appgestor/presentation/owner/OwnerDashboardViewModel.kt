package com.gestorplus.appgestor.presentation.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.data.local.entity.BookingEntity
import com.gestorplus.appgestor.data.repository.BookingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OwnerDashboardViewModel(
    private val repository: BookingRepository
) : ViewModel() {

    // Escuchamos los cambios en Room de forma reactiva
    val bookings: StateFlow<List<BookingEntity>> = repository.getBookings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onAcceptBooking(bookingId: String) {
        viewModelScope.launch {
            repository.updateStatus(bookingId, "CONFIRMED")
        }
    }

    fun onRejectBooking(bookingId: String) {
        viewModelScope.launch {
            repository.updateStatus(bookingId, "REJECTED")
        }
    }
}
