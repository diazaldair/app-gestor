package com.gestorplus.appgestor.owner.working_hours.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.owner.setup_schedule.domain.model.Shift
import com.gestorplus.appgestor.owner.setup_schedule.domain.repository.SetupScheduleRepository
import com.gestorplus.appgestor.owner.working_hours.presentation.state.WorkingHoursEfffect
import com.gestorplus.appgestor.owner.working_hours.presentation.state.WorkingHoursEvent
import com.gestorplus.appgestor.owner.working_hours.presentation.state.WorkingHoursUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class WorkingHoursViewModel(
    private val repository: SetupScheduleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkingHoursUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<WorkingHoursEfffect>()
    val effect: SharedFlow<WorkingHoursEfffect> = _effect.asSharedFlow()

    init {
        observeShifts()
        refreshShiftsFromRemote()
    }

    fun onEvent(event: WorkingHoursEvent) {
        when (event) {
            WorkingHoursEvent.LoadShifts -> { 
                refreshShiftsFromRemote()
            }
            is WorkingHoursEvent.AddShift -> {
                _uiState.update { it.copy(shifts = it.shifts + event.shift.copy(id = "new_${it.shifts.size + 1}")) }
            }
            is WorkingHoursEvent.UpdateShift -> {
                _uiState.update { state ->
                    state.copy(shifts = state.shifts.map { if (it.id == event.shift.id) event.shift else it })
                }
            }
            is WorkingHoursEvent.DeleteShift -> {
                _uiState.update { state ->
                    state.copy(shifts = state.shifts.filter { it.id != event.shiftId })
                }
            }
            is WorkingHoursEvent.ToggleDayInShift -> {
                _uiState.update { state ->
                    state.copy(shifts = state.shifts.map { shift ->
                        if (shift.id == event.shiftId) {
                            val newDays = if (shift.days.contains(event.day)) {
                                shift.days - event.day
                            } else {
                                shift.days + event.day
                            }
                            shift.copy(days = newDays)
                        } else shift
                    })
                }
            }
            is WorkingHoursEvent.TimeChanged -> {
                _uiState.update { state ->
                    state.copy(shifts = state.shifts.map { shift ->
                        if (shift.id == event.shiftId) {
                            if (event.isStart) shift.copy(startTime = event.time)
                            else shift.copy(endTime = event.time)
                        } else shift
                    })
                }
            }
            WorkingHoursEvent.SaveChanges -> saveChanges()
        }
    }

    private fun observeShifts() {
        viewModelScope.launch {
            repository.getShifts().collectLatest { loadedShifts ->
                _uiState.update { it.copy(
                    shifts = loadedShifts,
                    isLoading = false
                )}
            }
        }
    }

    private fun refreshShiftsFromRemote() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.refreshShifts().onFailure { error ->
                _effect.emit(WorkingHoursEfffect.ShowSnackbar("No se pudo sincronizar: ${error.message}"))
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun saveChanges() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            repository.saveDetailedShifts(_uiState.value.shifts).onSuccess {
                _uiState.update { it.copy(isSaving = false) }
                _effect.emit(WorkingHoursEfffect.ShowSnackbar("Horarios actualizados correctamente"))
                delay(500)
                _effect.emit(WorkingHoursEfffect.NavigateBack)
            }.onFailure { error ->
                _uiState.update { it.copy(isSaving = false) }
                _effect.emit(WorkingHoursEfffect.ShowSnackbar(error.message ?: "Error al guardar"))
            }
        }
    }
}
