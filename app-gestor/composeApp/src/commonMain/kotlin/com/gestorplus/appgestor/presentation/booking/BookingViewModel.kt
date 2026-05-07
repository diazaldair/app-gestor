package com.gestorplus.appgestor.presentation.booking

import com.gestorplus.appgestor.domain.booking.model.SlotPeriod
import com.gestorplus.appgestor.domain.booking.usecase.ConfirmBookingUseCase
import com.gestorplus.appgestor.domain.booking.usecase.GetAvailableSlotsUseCase
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

    private val _state = MutableStateFlow(BookingState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<BookingEffect>()
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
            is BookingEvent.OnConfirmBooking -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    val result = confirmBookingUseCase(
                        date = _state.value.selectedDate,
                        slot = _state.value.selectedTimeSlot ?: ""
                    )
                    _state.update { it.copy(isLoading = false) }
                    if (result.isSuccess) {
                        _effect.emit(BookingEffect.BookingConfirmed)
                    } else {
                        _effect.emit(BookingEffect.ShowError("Failed to confirm booking"))
                    }
                }
            }
            is BookingEvent.OnBackClicked -> {
                viewModelScope.launch {
                    _effect.emit(BookingEffect.NavigateBack)
                }
            }
        }
    }
}
