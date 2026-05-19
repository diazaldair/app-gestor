package com.gestorplus.appgestor.auth.presentation.landing.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.auth.presentation.landing.state.LandingEfffect
import com.gestorplus.appgestor.auth.presentation.landing.state.LandingEvent
import com.gestorplus.appgestor.auth.presentation.landing.state.LandingUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LandingViewModel : ViewModel() {

    private val _state = MutableStateFlow(LandingUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LandingEfffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: LandingEvent) {
        viewModelScope.launch {
            when (event) {
                LandingEvent.OnPatientSelected -> {
                    _effect.emit(LandingEfffect.NavigateToPatientFlow)
                }
                LandingEvent.OnProfessionalSelected -> {
                    _effect.emit(LandingEfffect.NavigateToProfessionalFlow)
                }
            }
        }
    }
}
