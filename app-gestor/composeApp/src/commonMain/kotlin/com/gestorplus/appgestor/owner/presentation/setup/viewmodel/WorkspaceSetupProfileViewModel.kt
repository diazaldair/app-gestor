package com.gestorplus.appgestor.owner.presentation.setup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.owner.presentation.setup.state.WorkspaceSetupProfileEfffect
import com.gestorplus.appgestor.owner.presentation.setup.state.WorkspaceSetupProfileEvent
import com.gestorplus.appgestor.owner.presentation.setup.state.WorkspaceSetupProfileUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkspaceSetupProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow(WorkspaceSetupProfileUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<WorkspaceSetupProfileEfffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: WorkspaceSetupProfileEvent) {
        viewModelScope.launch {
            when (event) {
                is WorkspaceSetupProfileEvent.ClinicNameChanged -> {
                    _state.update { it.copy(clinicName = event.value, errorMessage = null) }
                }
                is WorkspaceSetupProfileEvent.FullNameChanged -> {
                    _state.update { it.copy(fullName = event.value, errorMessage = null) }
                }
                is WorkspaceSetupProfileEvent.InputSpecialityChanged -> {
                    _state.update { it.copy(inputSpeciality = event.value) }
                }
                WorkspaceSetupProfileEvent.AddSpecialityClicked -> {
                    val currentInput = _state.value.inputSpeciality.trim()
                    if (currentInput.isNotEmpty() && !_state.value.specialities.contains(currentInput)) {
                        _state.update {
                            it.copy(
                                specialities = it.specialities + currentInput,
                                inputSpeciality = ""
                            )
                        }
                    }
                }
                is WorkspaceSetupProfileEvent.RemoveSpecialityClicked -> {
                    _state.update {
                        it.copy(specialities = it.specialities - event.speciality)
                    }
                }
                is WorkspaceSetupProfileEvent.BiographyChanged -> {
                    _state.update { it.copy(biography = event.value) }
                }
                WorkspaceSetupProfileEvent.FixLocationClicked -> {
                    _state.update {
                        it.copy(
                            isLocationFixed = true,
                            exactAddress = "Av. Arce #123, La Paz",
                            references = "Edf. Multicentro, Piso 4"
                        )
                    }
                    _effect.emit(WorkspaceSetupProfileEfffect.ShowSnackbar("Ubicación actual fijada correctamente."))
                }
                is WorkspaceSetupProfileEvent.ExactAddressChanged -> {
                    _state.update { it.copy(exactAddress = event.value) }
                }
                is WorkspaceSetupProfileEvent.ReferencesChanged -> {
                    _state.update { it.copy(references = event.value) }
                }
                WorkspaceSetupProfileEvent.AddPhotoClicked -> {
                    val photoId = "simulated_photo_${System.currentTimeMillis()}"
                    _state.update {
                        it.copy(galleryImages = it.galleryImages + photoId)
                    }
                }
                is WorkspaceSetupProfileEvent.RemovePhotoClicked -> {
                    _state.update {
                        it.copy(galleryImages = it.galleryImages - event.image)
                    }
                }
                WorkspaceSetupProfileEvent.OnContinueClicked -> {
                    if (_state.value.clinicName.isBlank() || _state.value.fullName.isBlank()) {
                        _state.update { it.copy(errorMessage = "Por favor, completa los campos requeridos.") }
                    } else {
                        _state.update { it.copy(isLoading = true) }
                        kotlinx.coroutines.delay(1000)
                        _state.update { it.copy(isLoading = false) }
                        _effect.emit(WorkspaceSetupProfileEfffect.NavigateToServices)
                    }
                }
                WorkspaceSetupProfileEvent.OnBackClicked -> {
                    _effect.emit(WorkspaceSetupProfileEfffect.NavigateBack)
                }
            }
        }
    }
}
