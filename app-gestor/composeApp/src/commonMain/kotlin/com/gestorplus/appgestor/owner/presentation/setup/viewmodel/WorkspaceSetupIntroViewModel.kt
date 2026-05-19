package com.gestorplus.appgestor.owner.presentation.setup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.owner.presentation.setup.state.WorkspaceSetupIntroEfffect
import com.gestorplus.appgestor.owner.presentation.setup.state.WorkspaceSetupIntroEvent
import com.gestorplus.appgestor.owner.presentation.setup.state.WorkspaceSetupIntroUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkspaceSetupIntroViewModel : ViewModel() {

    private val _state = MutableStateFlow(WorkspaceSetupIntroUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<WorkspaceSetupIntroEfffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: WorkspaceSetupIntroEvent) {
        viewModelScope.launch {
            when (event) {
                WorkspaceSetupIntroEvent.OnCreateWorkspaceClicked -> {
                    _state.update { it.copy(isLoading = true) }
                    // Simular preparación de workspace (delay)
                    kotlinx.coroutines.delay(800)
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(WorkspaceSetupIntroEfffect.NavigateToNextStep)
                }
            }
        }
    }
}
