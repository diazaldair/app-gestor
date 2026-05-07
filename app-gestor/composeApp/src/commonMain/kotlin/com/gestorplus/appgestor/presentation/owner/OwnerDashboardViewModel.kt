package com.gestorplus.appgestor.presentation.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.data.datasource.FirebaseManager
import com.gestorplus.appgestor.data.local.entity.BookingEntity
import com.gestorplus.appgestor.data.repository.OwnerBookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OwnerDashboardViewModel(
    private val repository: OwnerBookingRepository,
    private val firebaseManager: FirebaseManager
) : ViewModel() {

    private val _firebaseLogs = MutableStateFlow<List<String>>(emptyList())
    val firebaseLogs = _firebaseLogs.asStateFlow()

    private val _isLogsLoading = MutableStateFlow(false)
    val isLogsLoading = _isLogsLoading.asStateFlow()

    init {
        viewModelScope.launch {
            repository.syncAllBookings()
        }
    }

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

    fun loadFirebaseLogs() {
        viewModelScope.launch {
            _isLogsLoading.value = true
            val logs = firebaseManager.getFirebaseLogs("app_logs")
            _firebaseLogs.value = logs.reversed() // Los más recientes primero
            _isLogsLoading.value = false
        }
    }
}
