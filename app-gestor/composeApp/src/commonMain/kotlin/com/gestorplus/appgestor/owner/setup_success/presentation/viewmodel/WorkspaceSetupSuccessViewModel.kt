package com.gestorplus.appgestor.owner.setup_success.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.owner.setup_success.domain.usecase.GetRegisteredClinicNameUseCase
import com.gestorplus.appgestor.owner.setup_success.presentation.state.WorkspaceSetupSuccessEfffect
import com.gestorplus.appgestor.owner.setup_success.presentation.state.WorkspaceSetupSuccessEvent
import com.gestorplus.appgestor.owner.setup_success.presentation.state.WorkspaceSetupSuccessUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkspaceSetupSuccessViewModel(
    private val getRegisteredClinicNameUseCase: GetRegisteredClinicNameUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WorkspaceSetupSuccessUiState())
    val state: StateFlow<WorkspaceSetupSuccessUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<WorkspaceSetupSuccessEfffect>()
    val effect: SharedFlow<WorkspaceSetupSuccessEfffect> = _effect.asSharedFlow()

    init {
        onEvent(WorkspaceSetupSuccessEvent.OnLoadData)
    }

    fun onEvent(event: WorkspaceSetupSuccessEvent) {
        when (event) {
            WorkspaceSetupSuccessEvent.OnLoadData -> loadClinicName()
            WorkspaceSetupSuccessEvent.OnGoToConsoleClicked -> sendEffect(WorkspaceSetupSuccessEfffect.NavigateToDashboard)
        }
    }

    private fun loadClinicName() {
        viewModelScope.launch {
            val result = getRegisteredClinicNameUseCase()
            result.onSuccess { name ->
                _state.update { it.copy(clinicName = name, isLoading = false) }
            }.onFailure {
                _state.update { it.copy(clinicName = "Tu Clínica", isLoading = false) }
            }
        }
    }

    private fun sendEffect(effect: WorkspaceSetupSuccessEfffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
