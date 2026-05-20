package com.gestorplus.appgestor.clinicProfile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.clinicProfile.domain.model.ClinicProfile
import com.gestorplus.appgestor.clinicProfile.domain.usecase.GetClinicProfileUseCase
import com.gestorplus.appgestor.clinicProfile.domain.usecase.UpdateClinicProfileUseCase
import com.gestorplus.appgestor.clinicProfile.presentation.state.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ClinicProfileViewModel(
    private val getClinicProfileUseCase: GetClinicProfileUseCase,
    private val updateClinicProfileUseCase: UpdateClinicProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClinicProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ClinicProfileEfffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getClinicProfileUseCase().collect { profile ->
                _uiState.update { it.copy(profile = profile, isLoading = false) }
            }
        }
    }

    fun onEvent(event: ClinicProfileEvent) {
        when (event) {
            is ClinicProfileEvent.OnNameChanged -> {
                _uiState.update { it.copy(profile = it.profile.copy(name = event.name)) }
            }
            is ClinicProfileEvent.OnBiographyChanged -> {
                _uiState.update { it.copy(profile = it.profile.copy(biography = event.biography)) }
            }
            is ClinicProfileEvent.OnAddSpecialty -> {
                val current = _uiState.value.profile.specialties.toMutableList()
                if (!current.contains(event.specialty)) {
                    current.add(event.specialty)
                    _uiState.update { it.copy(profile = it.profile.copy(specialties = current)) }
                }
            }
            is ClinicProfileEvent.OnRemoveSpecialty -> {
                val current = _uiState.value.profile.specialties.toMutableList()
                current.remove(event.specialty)
                _uiState.update { it.copy(profile = it.profile.copy(specialties = current)) }
            }
            is ClinicProfileEvent.OnAddressChanged -> {
                _uiState.update { it.copy(profile = it.profile.copy(address = event.address)) }
            }
            ClinicProfileEvent.OnSaveClicked -> {
                saveProfile()
            }
        }
    }

    private fun saveProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            updateClinicProfileUseCase(_uiState.value.profile)
            _uiState.update { it.copy(isSaving = false) }
            _effect.emit(ClinicProfileEfffect.ShowToast("Perfil actualizado correctamente"))
        }
    }
}
