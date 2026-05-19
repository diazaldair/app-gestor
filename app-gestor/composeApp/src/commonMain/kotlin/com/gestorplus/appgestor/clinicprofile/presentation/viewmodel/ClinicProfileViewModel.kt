package com.gestorplus.appgestor.clinicprofile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.clinicprofile.domain.model.ClinicProfile
import com.gestorplus.appgestor.clinicprofile.domain.model.ClinicService
import com.gestorplus.appgestor.clinicprofile.presentation.state.ClinicProfileEfffect
import com.gestorplus.appgestor.clinicprofile.presentation.state.ClinicProfileEvent
import com.gestorplus.appgestor.clinicprofile.presentation.state.ClinicProfileUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClinicProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow(ClinicProfileUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ClinicProfileEfffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // Mock data as requested for visual implementation
            val mockProfile = ClinicProfile(
                name = "SoloBook Medical Center",
                subtitle = "Centro de Especialidades Médicas",
                bio = "Brindamos atención médica de alta calidad con un equipo de especialistas dedicados a tu bienestar integral.",
                location = "Av. Principal 123, Ciudad Salud",
                specialties = listOf("Cardiología", "Pediatría", "Dermatología", "Odontología"),
                services = listOf(
                    ClinicService("1", "Consulta General", "30 min", "$50", "Examen físico y diagnóstico inicial."),
                    ClinicService("2", "Limpieza Dental", "45 min", "$80", "Limpieza profesional profunda."),
                    ClinicService("3", "Chequeo Preventivo", "60 min", "$120", "Evaluación completa de salud.")
                )
            )
            _state.update { it.copy(isLoading = false, clinicProfile = mockProfile) }
        }
    }

    fun onEvent(event: ClinicProfileEvent) {
        when (event) {
            is ClinicProfileEvent.OnBackClicked -> {
                viewModelScope.launch { _effect.emit(ClinicProfileEfffect.NavigateBack) }
            }
            is ClinicProfileEvent.OnReserveService -> {
                viewModelScope.launch { _effect.emit(ClinicProfileEfffect.NavigateToBooking(event.serviceId)) }
            }
        }
    }
}
