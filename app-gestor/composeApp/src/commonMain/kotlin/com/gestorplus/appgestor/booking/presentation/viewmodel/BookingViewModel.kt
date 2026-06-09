package com.gestorplus.appgestor.booking.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.booking.domain.model.SlotPeriod
import com.gestorplus.appgestor.booking.domain.usecase.ConfirmBookingUseCase
import com.gestorplus.appgestor.booking.domain.usecase.GetAvailableSlotsUseCase
import com.gestorplus.appgestor.booking.presentation.state.*
import com.gestorplus.appgestor.core.data.local.dao.BookingDraftDao
import com.gestorplus.appgestor.core.data.local.entity.BookingDraftEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookingViewModel(
    private val clinicId: String,
    private val serviceId: String,
    private val getAvailableSlotsUseCase: GetAvailableSlotsUseCase,
    private val confirmBookingUseCase: ConfirmBookingUseCase,
    private val draftDao: BookingDraftDao
) : ViewModel() {

    private val _state = MutableStateFlow(BookingUiState(clinicId = clinicId, serviceId = serviceId))
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
            is BookingEvent.OnConfirmBooking -> {
                viewModelScope.launch {
                    val currentSlot = _state.value.selectedTimeSlot
                    if (currentSlot == null) {
                        _effect.emit(BookingEfffect.ShowError("Por favor selecciona un horario"))
                        return@launch
                    }

                    // Guardamos en Room antes de pasar a la pantalla de desglose
                    draftDao.saveDraft(
                        BookingDraftEntity(
                            clinicId = clinicId,
                            serviceId = serviceId,
                            selectedDate = _state.value.selectedDate,
                            selectedMonth = _state.value.selectedMonth,
                            selectedTimeSlot = currentSlot
                        )
                    )
                    
                    _effect.emit(BookingEfffect.BookingConfirmed)
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
