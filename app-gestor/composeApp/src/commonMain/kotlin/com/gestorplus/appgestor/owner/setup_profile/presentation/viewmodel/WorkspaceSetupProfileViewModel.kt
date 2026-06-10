package com.gestorplus.appgestor.owner.setup_profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.owner.setup_profile.presentation.state.WorkspaceSetupProfileEfffect
import com.gestorplus.appgestor.owner.setup_profile.presentation.state.WorkspaceSetupProfileEvent
import com.gestorplus.appgestor.owner.setup_profile.presentation.state.WorkspaceSetupProfileUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.gestorplus.appgestor.owner.setup_profile.domain.usecase.SaveWorkspaceProfileUseCase
import com.gestorplus.appgestor.owner.setup_profile.domain.model.WorkspaceProfile

class WorkspaceSetupProfileViewModel(
    private val saveWorkspaceProfileUseCase: SaveWorkspaceProfileUseCase
) : ViewModel() {

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
                is WorkspaceSetupProfileEvent.ExactAddressChanged -> {
                    _state.update { it.copy(exactAddress = event.value) }
                }
                is WorkspaceSetupProfileEvent.ReferencesChanged -> {
                    _state.update { it.copy(references = event.value) }
                }
                is WorkspaceSetupProfileEvent.LocationUrlChanged -> {
                    _state.update { it.copy(locationUrl = event.value) }
                }
                is WorkspaceSetupProfileEvent.PhotoSelected -> {
                    if (event.uri.isNotBlank()) {
                        _state.update {
                            it.copy(galleryImages = it.galleryImages + event.uri)
                        }
                    }
                }
                is WorkspaceSetupProfileEvent.RemovePhotoClicked -> {
                    _state.update {
                        it.copy(galleryImages = it.galleryImages - event.image)
                    }
                }
                is WorkspaceSetupProfileEvent.DepartmentSelected -> {
                    _state.update { it.copy(selectedDepartment = event.department) }
                }
                WorkspaceSetupProfileEvent.OnContinueClicked -> {
                    val currentState = _state.value
                    _state.update { it.copy(isLoading = true) }
                    
                    val profile = WorkspaceProfile(
                        clinicName = currentState.clinicName,
                        fullName = currentState.fullName,
                        specialities = currentState.specialities,
                        biography = currentState.biography,
                        exactAddress = currentState.exactAddress,
                        references = currentState.references,
                        galleryImages = currentState.galleryImages,
                        locationUrl = currentState.locationUrl,
                        latitude = null,
                        longitude = null
                    )
                    
                    val result = saveWorkspaceProfileUseCase(profile)
                    _state.update { it.copy(isLoading = false) }
                    
                    if (result.isSuccess) {
                        _effect.emit(WorkspaceSetupProfileEfffect.NavigateToServices)
                    } else {
                        val errorMsg = result.exceptionOrNull()?.message ?: "Error al guardar"
                        _state.update { it.copy(errorMessage = errorMsg) }
                        _effect.emit(WorkspaceSetupProfileEfffect.ShowSnackbar(errorMsg))
                    }
                }
                WorkspaceSetupProfileEvent.OnBackClicked -> {
                    _effect.emit(WorkspaceSetupProfileEfffect.NavigateBack)
                }
            }
        }
    }
}
