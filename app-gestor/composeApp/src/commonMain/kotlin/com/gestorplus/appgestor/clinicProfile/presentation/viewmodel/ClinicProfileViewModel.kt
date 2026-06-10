package com.gestorplus.appgestor.clinicProfile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.clinicProfile.domain.usecase.GetClinicProfileUseCase
import com.gestorplus.appgestor.clinicProfile.domain.usecase.UpdateClinicProfileUseCase
import com.gestorplus.appgestor.clinicProfile.presentation.state.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ClinicProfileViewModel(
    private val getClinicProfileUseCase: GetClinicProfileUseCase,
    private val updateClinicProfileUseCase: UpdateClinicProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ClinicProfileUiState())
    val state: StateFlow<ClinicProfileUiState> = _state.asStateFlow()

    private val _efffect = MutableSharedFlow<ClinicProfileEfffect>()
    val efffect: SharedFlow<ClinicProfileEfffect> = _efffect.asSharedFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getClinicProfileUseCase().collect { profile ->
                _state.update { it.copy(profile = profile, isLoading = false) }
            }
        }
    }

    fun onEvent(event: ClinicProfileEvent) {
        when (event) {
            is ClinicProfileEvent.NameChanged -> {
                _state.update { it.copy(profile = it.profile.copy(name = event.name)) }
            }
            is ClinicProfileEvent.BiographyChanged -> {
                _state.update { it.copy(profile = it.profile.copy(biography = event.bio)) }
            }
            is ClinicProfileEvent.AddressChanged -> {
                _state.update { it.copy(profile = it.profile.copy(address = event.address)) }
            }
            is ClinicProfileEvent.LocationUrlChanged -> {
                _state.update { it.copy(profile = it.profile.copy(locationUrl = event.url)) }
            }
            is ClinicProfileEvent.SpecialityInputChanged -> {
                _state.update { it.copy(newSpeciality = event.value) }
            }
            ClinicProfileEvent.AddSpecialityClicked -> {
                val current = _state.value.newSpeciality.trim()
                if (current.isNotEmpty()) {
                    _state.update {
                        it.copy(
                            profile = it.profile.copy(specialties = it.profile.specialties + current),
                            newSpeciality = ""
                        )
                    }
                }
            }
            is ClinicProfileEvent.RemoveSpecialityClicked -> {
                _state.update {
                    it.copy(
                        profile = it.profile.copy(specialties = it.profile.specialties - event.speciality)
                    )
                }
            }
            ClinicProfileEvent.SaveClicked -> {
                saveProfile()
            }
            ClinicProfileEvent.BackClicked -> {
                viewModelScope.launch { _efffect.emit(ClinicProfileEfffect.NavigateBack) }
            }
        }
    }

    private fun saveProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            updateClinicProfileUseCase(_state.value.profile)
            _state.update { it.copy(isSaving = false) }
            _efffect.emit(ClinicProfileEfffect.ShowMessage("Perfil actualizado"))
        }
    }
}
