package com.gestorplus.appgestor.owner.setup_service.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.owner.setup_service.domain.model.WorkspaceService
import com.gestorplus.appgestor.owner.setup_service.domain.usecase.SaveWorkspaceServiceUseCase
import com.gestorplus.appgestor.owner.setup_service.presentation.state.DurationOption
import com.gestorplus.appgestor.owner.setup_service.presentation.state.WorkspaceSetupServiceEfffect
import com.gestorplus.appgestor.owner.setup_service.presentation.state.WorkspaceSetupServiceEvent
import com.gestorplus.appgestor.owner.setup_service.presentation.state.WorkspaceSetupServiceUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkspaceSetupServiceViewModel(
    private val saveWorkspaceServiceUseCase: SaveWorkspaceServiceUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WorkspaceSetupServiceUiState())
    val state: StateFlow<WorkspaceSetupServiceUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<WorkspaceSetupServiceEfffect>()
    val effect: SharedFlow<WorkspaceSetupServiceEfffect> = _effect.asSharedFlow()

    fun onEvent(event: WorkspaceSetupServiceEvent) {
        when (event) {
            is WorkspaceSetupServiceEvent.ServiceNameChanged -> _state.update { it.copy(serviceName = event.name, errorMessage = null) }
            is WorkspaceSetupServiceEvent.DescriptionChanged -> _state.update { it.copy(description = event.description, errorMessage = null) }
            is WorkspaceSetupServiceEvent.PriceChanged -> {
                // Allow only numbers and a single decimal point
                val regex = Regex("^\\d*\\.?\\d*$")
                if (event.price.matches(regex)) {
                    _state.update { it.copy(price = event.price, errorMessage = null) }
                }
            }
            is WorkspaceSetupServiceEvent.DurationOptionSelected -> _state.update { it.copy(selectedDurationOption = event.option) }
            WorkspaceSetupServiceEvent.IncrementCustomHours -> _state.update { 
                it.copy(customHours = minOf(it.customHours + 1, 12)) 
            }
            WorkspaceSetupServiceEvent.DecrementCustomHours -> _state.update { 
                it.copy(customHours = maxOf(it.customHours - 1, 0)) 
            }
            WorkspaceSetupServiceEvent.IncrementCustomMinutes -> _state.update { 
                it.copy(customMinutes = minOf(it.customMinutes + 5, 55)) 
            }
            WorkspaceSetupServiceEvent.DecrementCustomMinutes -> _state.update { 
                it.copy(customMinutes = maxOf(it.customMinutes - 5, 0)) 
            }
            WorkspaceSetupServiceEvent.OnBackClicked -> sendEffect(WorkspaceSetupServiceEfffect.NavigateBack)
            WorkspaceSetupServiceEvent.OnContinueClicked -> submitService()
        }
    }

    private fun submitService() {
        val currentState = _state.value
        
        if (currentState.serviceName.isBlank()) {
            _state.update { it.copy(errorMessage = "El nombre del servicio es obligatorio.") }
            return
        }
        
        val priceDouble = currentState.price.toDoubleOrNull() ?: 0.0
        
        val totalMinutes = if (currentState.selectedDurationOption == DurationOption.CUSTOM) {
            (currentState.customHours * 60) + currentState.customMinutes
        } else {
            currentState.selectedDurationOption.minutes ?: 15
        }
        
        if (totalMinutes <= 0) {
            _state.update { it.copy(errorMessage = "La duración debe ser mayor a 0 minutos.") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            
            val service = WorkspaceService(
                name = currentState.serviceName,
                description = currentState.description,
                price = priceDouble,
                currency = currentState.currency,
                durationMinutes = totalMinutes
            )
            
            val result = saveWorkspaceServiceUseCase(service)
            
            result.onSuccess {
                _state.update { it.copy(isLoading = false) }
                sendEffect(WorkspaceSetupServiceEfffect.NavigateToNextStep)
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, errorMessage = error.message ?: "Error al guardar el servicio") }
                sendEffect(WorkspaceSetupServiceEfffect.ShowSnackbar(error.message ?: "Error desconocido"))
            }
        }
    }

    private fun sendEffect(effect: WorkspaceSetupServiceEfffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
