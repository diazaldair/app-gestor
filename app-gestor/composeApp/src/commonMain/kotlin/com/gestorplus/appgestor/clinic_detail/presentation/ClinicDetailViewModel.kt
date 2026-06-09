package com.gestorplus.appgestor.clinic_detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.clinic_detail.data.ClinicDetailRepositoryImpl
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ClinicDetailViewModel(
    private val repository: ClinicDetailRepositoryImpl,
    private val clinicId: String
) : ViewModel() {

    private val _state = MutableStateFlow(ClinicDetailState())
    val state: StateFlow<ClinicDetailState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ClinicDetailEffect>()
    val effect: SharedFlow<ClinicDetailEffect> = _effect.asSharedFlow()

    init {
        loadData()
        syncData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            combine(
                repository.getClinic(clinicId),
                repository.getServices(clinicId)
            ) { clinic, services ->
                _state.update {
                    it.copy(
                        clinic = clinic,
                        services = services,
                        isLoading = false
                    )
                }
            }.collect()
        }
    }

    private fun syncData() {
        viewModelScope.launch {
            repository.syncServices(clinicId)
        }
    }

    fun onEvent(event: ClinicDetailEvent) {
        when (event) {
            is ClinicDetailEvent.OnBackClicked -> {
                viewModelScope.launch { _effect.emit(ClinicDetailEffect.NavigateBack) }
            }
            is ClinicDetailEvent.OnBookServiceClicked -> {
                viewModelScope.launch { 
                    _effect.emit(ClinicDetailEffect.NavigateToBooking(clinicId, event.serviceId))
                }
            }
        }
    }
}

sealed interface ClinicDetailEvent {
    object OnBackClicked : ClinicDetailEvent
    data class OnBookServiceClicked(val serviceId: String) : ClinicDetailEvent
}

sealed interface ClinicDetailEffect {
    object NavigateBack : ClinicDetailEffect
    data class NavigateToBooking(val clinicId: String, val serviceId: String) : ClinicDetailEffect
}
