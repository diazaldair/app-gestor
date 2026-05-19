package com.gestorplus.appgestor.datetimeselector.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.datetimeselector.domain.model.TimePeriod
import com.gestorplus.appgestor.datetimeselector.domain.model.TimeSlot
import com.gestorplus.appgestor.datetimeselector.presentation.state.DateTimeSelectorEfffect
import com.gestorplus.appgestor.datetimeselector.presentation.state.DateTimeSelectorEvent
import com.gestorplus.appgestor.datetimeselector.presentation.state.DateTimeSelectorUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DateTimeSelectorViewModel : ViewModel() {

    private val _state = MutableStateFlow(DateTimeSelectorUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<DateTimeSelectorEfffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadSlots(_state.value.selectedDate)
    }

    private fun loadSlots(date: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // Mocking availability based on the reference image
            val morning = listOf(
                TimeSlot("1", "09:00 AM", true, TimePeriod.MORNING),
                TimeSlot("2", "09:30 AM", false, TimePeriod.MORNING),
                TimeSlot("3", "10:00 AM", true, TimePeriod.MORNING),
                TimeSlot("4", "10:30 AM", true, TimePeriod.MORNING),
                TimeSlot("5", "11:00 AM", true, TimePeriod.MORNING),
                TimeSlot("6", "11:30 AM", true, TimePeriod.MORNING)
            )
            val afternoon = listOf(
                TimeSlot("7", "01:00 PM", true, TimePeriod.AFTERNOON),
                TimeSlot("8", "01:30 PM", true, TimePeriod.AFTERNOON),
                TimeSlot("9", "02:00 PM", true, TimePeriod.AFTERNOON),
                TimeSlot("10", "02:30 PM", true, TimePeriod.AFTERNOON),
                TimeSlot("11", "03:00 PM", true, TimePeriod.AFTERNOON),
                TimeSlot("12", "04:00 PM", true, TimePeriod.AFTERNOON)
            )
            _state.update { it.copy(
                isLoading = false,
                morningSlots = morning,
                afternoonSlots = afternoon
            ) }
        }
    }

    fun onEvent(event: DateTimeSelectorEvent) {
        when (event) {
            is DateTimeSelectorEvent.OnDateSelected -> {
                _state.update { it.copy(selectedDate = event.date, selectedTimeSlot = null) }
                loadSlots(event.date)
            }
            is DateTimeSelectorEvent.OnTimeSlotSelected -> {
                _state.update { it.copy(selectedTimeSlot = event.slotId) }
            }
            is DateTimeSelectorEvent.OnConfirmClicked -> {
                val selectedSlot = (_state.value.morningSlots + _state.value.afternoonSlots)
                    .find { it.id == _state.value.selectedTimeSlot }
                
                if (selectedSlot != null) {
                    viewModelScope.launch {
                        _effect.emit(DateTimeSelectorEfffect.BookingConfirmed(_state.value.selectedDate, selectedSlot.time))
                    }
                }
            }
            is DateTimeSelectorEvent.OnBackClicked -> {
                viewModelScope.launch { _effect.emit(DateTimeSelectorEfffect.NavigateBack) }
            }
        }
    }
}
