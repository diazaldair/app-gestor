package com.gestorplus.appgestor.booking.presentation.viewmodel

import com.gestorplus.appgestor.booking.domain.model.SlotPeriod
import com.gestorplus.appgestor.booking.domain.usecase.ConfirmBookingUseCase
import com.gestorplus.appgestor.booking.domain.usecase.GetAvailableSlotsUseCase
import com.gestorplus.appgestor.booking.presentation.state.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookingViewModel(
    private val getAvailableSlotsUseCase: GetAvailableSlotsUseCase,
    private val confirmBookingUseCase: ConfirmBookingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BookingUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<BookingEfffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadAvailableSlots(_state.value.selectedDate)
    }

    private fun loadAvailableSlots(date: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val slots = getAvailableSlotsUseCase(date)
            _state.update { state ->
                state.copy(
                    isLoading = false,
                    timeSlotsMorning = slots.filter { it.period == SlotPeriod.MORNING }.map { it.time },
                    timeSlotsAfternoon = slots.filter { it.period == SlotPeriod.AFTERNOON }.map { it.time }
                )
            }
        }
    }

    fun onEvent(event: BookingEvent) {
        when (event) {
            is BookingEvent.OnDateSelected -> {
                _state.update { it.copy(selectedDate = event.date) }
                loadAvailableSlots(event.date)
            }
            is BookingEvent.OnTimeSlotSelected -> {
                _state.update { it.copy(selectedTimeSlot = event.slot) }
            }
            is BookingEvent.OnNotesChanged -> {
                _state.update { it.copy(additionalNotes = event.notes) }
            }
            is BookingEvent.OnConfirmBooking -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    val result = confirmBookingUseCase(
                        date = _state.value.selectedDate,
                        slot = _state.value.selectedTimeSlot ?: "",
                        notes = _state.value.additionalNotes,
                        service = _state.value.serviceName,
                        doctor = _state.value.doctorName,
                        totalPrice = _state.value.totalPrice
                    )
                    _state.update { it.copy(isLoading = false) }
                    if (result.isSuccess) {
                        _effect.emit(BookingEfffect.BookingConfirmed)
                    } else {
                        _effect.emit(BookingEfffect.ShowError("Failed to confirm booking"))
                    }
                }
            }
            is BookingEvent.OnBackClicked -> {
                viewModelScope.launch {
                    _effect.emit(BookingEfffect.NavigateBack)
                }
            }
        }
    }
}
