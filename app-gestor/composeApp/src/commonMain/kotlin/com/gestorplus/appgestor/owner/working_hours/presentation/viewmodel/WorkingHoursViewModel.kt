package com.gestorplus.appgestor.owner.working_hours.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.owner.schedule.domain.model.*
import com.gestorplus.appgestor.owner.schedule.domain.usecase.*
import com.gestorplus.appgestor.owner.working_hours.presentation.state.WorkingHoursEffect
import com.gestorplus.appgestor.owner.working_hours.presentation.state.WorkingHoursEvent
import com.gestorplus.appgestor.owner.working_hours.presentation.state.WorkingHoursUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkingHoursViewModel(
    private val getShiftsUseCase: GetWorkingShiftsUseCase,
    private val saveShiftsUseCase: SaveWorkingShiftsUseCase,
    private val getMasterUseCase: GetMasterScheduleUseCase,
    private val saveMasterUseCase: SaveMasterScheduleUseCase,
    private val getAutoLunchUseCase: GetAutoLunchUseCase,
    private val saveAutoLunchUseCase: SaveAutoLunchUseCase,
    private val getTimingUseCase: GetTimingDefaultsUseCase,
    private val saveTimingUseCase: SaveTimingDefaultsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WorkingHoursUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<WorkingHoursEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadAll()
    }

    fun onEvent(event: WorkingHoursEvent) {
        when (event) {
            is WorkingHoursEvent.UpdateShift -> {
                val newShifts = _state.value.shifts.toMutableMap().apply { put(event.shift.dayOfWeek, event.shift) }
                _state.update { it.copy(shifts = newShifts) }
            }
            is WorkingHoursEvent.UpdateMasterSchedule -> _state.update { it.copy(masterSchedule = event.schedule) }
            is WorkingHoursEvent.UpdateAutoLunch -> _state.update { it.copy(autoLunch = event.config) }
            is WorkingHoursEvent.UpdateTimingDefaults -> _state.update { it.copy(timingDefaults = event.defaults) }
            WorkingHoursEvent.SaveAll -> saveAll()
            WorkingHoursEvent.LoadAll -> loadAll()
            WorkingHoursEvent.NavigateBack -> viewModelScope.launch { _effect.emit(WorkingHoursEffect.NavigateBack) }
            WorkingHoursEvent.ResetAll -> {
                _state.update {
                    it.copy(
                        masterSchedule = MasterSchedule(startTime = "09:00 AM", endTime = "05:00 PM", enabledDays = listOf(1,2,3,4,5)),
                        autoLunch = AutoLunchConfig(),
                        timingDefaults = TimingDefaults()
                    )
                }
                saveAll()
            }
            is WorkingHoursEvent.NavigateToExceptions -> viewModelScope.launch { _effect.emit(WorkingHoursEffect.NavigateToExceptions(event.date)) }
        }
    }

    private fun loadAll() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // Cargar shifts
            getShiftsUseCase().collect { shifts ->
                _state.update { it.copy(shifts = shifts.associateBy { it.dayOfWeek }, isLoading = false) }
            }
            // Cargar otros (son suspend, no flow)
            val master = getMasterUseCase()
            if (master != null) _state.update { it.copy(masterSchedule = master) }
            val lunch = getAutoLunchUseCase()
            if (lunch != null) _state.update { it.copy(autoLunch = lunch) }
            val timing = getTimingUseCase()
            if (timing != null) _state.update { it.copy(timingDefaults = timing) }
        }
    }

    private fun saveAll() {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, errorMessage = null, successMessage = null) }
            val shiftsList = _state.value.shifts.values.toList()
            val resultShifts = saveShiftsUseCase(shiftsList)
            val resultMaster = saveMasterUseCase(_state.value.masterSchedule)
            val resultLunch = saveAutoLunchUseCase(_state.value.autoLunch)
            val resultTiming = saveTimingUseCase(_state.value.timingDefaults)
            val allSuccess = listOf(resultShifts, resultMaster, resultLunch, resultTiming).all { it.isSuccess }
            _state.update { it.copy(isSaving = false) }
            if (allSuccess) {
                _effect.emit(WorkingHoursEffect.ShowSnackbar("Todos los cambios guardados"))
            } else {
                _effect.emit(WorkingHoursEffect.ShowSnackbar("Error al guardar algunos cambios"))
            }
        }
    }
}