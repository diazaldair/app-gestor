package com.gestorplus.appgestor.owner.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.owner.home.domain.usecase.GetHomeDataUseCase
import com.gestorplus.appgestor.owner.home.presentation.state.OwnerHomeEffect
import com.gestorplus.appgestor.owner.home.presentation.state.OwnerHomeEvent
import com.gestorplus.appgestor.owner.home.presentation.state.OwnerHomeUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OwnerHomeViewModel(
    private val getHomeDataUseCase: GetHomeDataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OwnerHomeUiState())
    val state: StateFlow<OwnerHomeUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<OwnerHomeEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadHomeData()
    }

    fun onEvent(event: OwnerHomeEvent) {
        when (event) {
            is OwnerHomeEvent.OnRefresh -> loadHomeData()
            is OwnerHomeEvent.OnServiceCatalogClicked -> emitEffect(OwnerHomeEffect.NavigateToServiceCatalog)
            is OwnerHomeEvent.OnShiftConfigClicked -> emitEffect(OwnerHomeEffect.NavigateToShiftConfig)
            is OwnerHomeEvent.OnStartConsultationClicked -> emitEffect(OwnerHomeEffect.NavigateToConsultation)
            is OwnerHomeEvent.OnViewAllClicked -> emitEffect(OwnerHomeEffect.NavigateToAllAppointments)
            is OwnerHomeEvent.OnAddAppointmentClicked -> emitEffect(OwnerHomeEffect.NavigateToAddAppointment)
            is OwnerHomeEvent.OnChatClicked -> emitEffect(OwnerHomeEffect.NavigateToChat(event.appointmentId))
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            getHomeDataUseCase().fold(
                onSuccess = { data ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        doctorName = data.doctorName,
                        totalAppointments = data.totalAppointments,
                        pendingAppointments = data.pendingAppointments,
                        nextAppointment = data.nextAppointment,
                        restOfDayAppointments = data.restOfDayAppointments
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Error desconocido"
                    )
                    emitEffect(OwnerHomeEffect.ShowError(error.message ?: "Error desconocido"))
                }
            )
        }
    }

    private fun emitEffect(effect: OwnerHomeEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}
