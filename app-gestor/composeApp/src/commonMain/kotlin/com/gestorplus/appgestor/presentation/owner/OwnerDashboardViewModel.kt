package com.gestorplus.appgestor.presentation.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.data.datasource.FirebaseManager
import com.gestorplus.appgestor.data.repository.OwnerBookingRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OwnerDashboardViewModel(
    private val repository: OwnerBookingRepository,
    private val firebaseManager: FirebaseManager
) : ViewModel() {

    private val _state = MutableStateFlow(OwnerDashboardState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<OwnerDashboardEffect>()
    val effect = _effect.asSharedFlow()

    init {
        observeBookings()
        refreshBookings()
    }

    private fun observeBookings() {
        repository.getBookings()
            .onEach { bookings ->
                _state.update { it.copy(bookings = bookings) }
            }
            .launchIn(viewModelScope)
    }

    private fun refreshBookings() {
        viewModelScope.launch {
            _state.update { it.copy(isSyncing = true) }
            try {
                repository.syncAllBookings()
            } catch (e: Exception) {
                _effect.emit(OwnerDashboardEffect.ShowSnackbar("Error syncing bookings"))
            } finally {
                _state.update { it.copy(isSyncing = false) }
            }
        }
    }

    fun onEvent(event: OwnerDashboardEvent) {
        when (event) {
            is OwnerDashboardEvent.OnAcceptBooking -> acceptBooking(event.bookingId)
            is OwnerDashboardEvent.OnRejectBooking -> rejectBooking(event.bookingId)
            OwnerDashboardEvent.OnLoadLogs -> loadFirebaseLogs()
            OwnerDashboardEvent.OnRefreshBookings -> refreshBookings()
            OwnerDashboardEvent.OnClearError -> _state.update { it.copy(error = null) }
        }
    }

    private fun acceptBooking(id: String) {
        viewModelScope.launch {
            repository.updateStatus(id, "CONFIRMED")
            _effect.emit(OwnerDashboardEffect.ShowSnackbar("Booking accepted"))
        }
    }

    private fun rejectBooking(id: String) {
        viewModelScope.launch {
            repository.updateStatus(id, "REJECTED")
            _effect.emit(OwnerDashboardEffect.ShowSnackbar("Booking rejected"))
        }
    }

    private fun loadFirebaseLogs() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingLogs = true) }
            try {
                val logs = firebaseManager.getFirebaseLogs("app_logs")
                _state.update { it.copy(firebaseLogs = logs.reversed(), isLoadingLogs = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoadingLogs = false) }
                _effect.emit(OwnerDashboardEffect.ShowSnackbar("Error loading logs"))
            }
        }
    }
}
