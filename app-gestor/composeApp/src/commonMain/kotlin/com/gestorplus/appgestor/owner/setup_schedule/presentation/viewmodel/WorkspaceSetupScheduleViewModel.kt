package com.gestorplus.appgestor.owner.setup_schedule.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.owner.setup_schedule.domain.model.WorkspaceSchedule
import com.gestorplus.appgestor.owner.setup_schedule.domain.usecase.SaveWorkspaceScheduleUseCase
import com.gestorplus.appgestor.owner.setup_schedule.presentation.state.WorkspaceSetupScheduleEfffect
import com.gestorplus.appgestor.owner.setup_schedule.presentation.state.WorkspaceSetupScheduleEvent
import com.gestorplus.appgestor.owner.setup_schedule.presentation.state.WorkspaceSetupScheduleUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkspaceSetupScheduleViewModel(
    private val saveWorkspaceScheduleUseCase: SaveWorkspaceScheduleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WorkspaceSetupScheduleUiState())
    val state: StateFlow<WorkspaceSetupScheduleUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<WorkspaceSetupScheduleEfffect>()
    val effect: SharedFlow<WorkspaceSetupScheduleEfffect> = _effect.asSharedFlow()

    fun onEvent(event: WorkspaceSetupScheduleEvent) {
        when (event) {
            is WorkspaceSetupScheduleEvent.DayToggled -> toggleDay(event.day)
            is WorkspaceSetupScheduleEvent.MorningStartChanged -> _state.update { it.copy(morningStart = event.time, errorMessage = null) }
            is WorkspaceSetupScheduleEvent.MorningEndChanged -> _state.update { it.copy(morningEnd = event.time, errorMessage = null) }
            is WorkspaceSetupScheduleEvent.AfternoonStartChanged -> _state.update { it.copy(afternoonStart = event.time, errorMessage = null) }
            is WorkspaceSetupScheduleEvent.AfternoonEndChanged -> _state.update { it.copy(afternoonEnd = event.time, errorMessage = null) }
            WorkspaceSetupScheduleEvent.OnBackClicked -> sendEffect(WorkspaceSetupScheduleEfffect.NavigateBack)
            WorkspaceSetupScheduleEvent.OnContinueClicked -> submitSchedule()
        }
    }

    private fun toggleDay(day: String) {
        val currentDays = _state.value.selectedDays.toMutableList()
        if (currentDays.contains(day)) {
            currentDays.remove(day)
        } else {
            currentDays.add(day)
        }
        _state.update { it.copy(selectedDays = currentDays, errorMessage = null) }
    }

    private fun submitSchedule() {
        val currentState = _state.value
        
        if (currentState.selectedDays.isEmpty()) {
            _state.update { it.copy(errorMessage = "Debes seleccionar al menos un día laboral.") }
            return
        }
        if (currentState.morningStart.isBlank() || currentState.morningEnd.isBlank()) {
            _state.update { it.copy(errorMessage = "Debes definir el horario de la mañana.") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            
            val schedule = WorkspaceSchedule(
                workingDays = currentState.selectedDays,
                morningStart = currentState.morningStart,
                morningEnd = currentState.morningEnd,
                afternoonStart = currentState.afternoonStart,
                afternoonEnd = currentState.afternoonEnd
            )

            val result = saveWorkspaceScheduleUseCase(schedule)
            
            result.onSuccess {
                _state.update { it.copy(isLoading = false) }
                sendEffect(WorkspaceSetupScheduleEfffect.NavigateToNextStep)
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, errorMessage = error.message ?: "Error al guardar el horario") }
                sendEffect(WorkspaceSetupScheduleEfffect.ShowSnackbar(error.message ?: "Error desconocido"))
            }
        }
    }

    private fun sendEffect(effect: WorkspaceSetupScheduleEfffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
