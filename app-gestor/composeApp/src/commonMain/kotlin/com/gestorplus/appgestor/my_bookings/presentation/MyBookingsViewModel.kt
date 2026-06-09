package com.gestorplus.appgestor.my_bookings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.my_bookings.data.PatientBookingRepositoryImpl
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MyBookingsViewModel(
    private val repository: PatientBookingRepositoryImpl
) : ViewModel() {

    private val _state = MutableStateFlow(MyBookingsState())
    val state: StateFlow<MyBookingsState> = _state.asStateFlow()

    init {
        loadBookings()
        syncData()
    }

    private fun loadBookings() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            combine(
                repository.getUpcomingBookings(),
                repository.getPastBookings()
            ) { upcoming, past ->
                _state.update { 
                    it.copy(
                        upcomingBookings = upcoming,
                        pastBookings = past,
                        isLoading = false
                    )
                }
            }.collect()
        }
    }

    private fun syncData() {
        viewModelScope.launch {
            repository.syncBookings()
        }
    }

    fun onTabSelected(tab: BookingTab) {
        _state.update { it.copy(selectedTab = tab) }
    }
}
