package com.gestorplus.appgestor.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.profile.domain.model.UserProfile
import com.gestorplus.appgestor.profile.domain.usecase.GetUserProfileUseCase
import com.gestorplus.appgestor.profile.domain.usecase.UpdateUserProfileUseCase
import com.gestorplus.appgestor.profile.presentation.state.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEfffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getUserProfileUseCase().collect { profile ->
                if (profile != null) {
                    _state.update {
                        it.copy(
                            name = profile.name,
                            email = profile.email,
                            phone = profile.phone,
                            description = profile.description,
                            imageUrl = profile.imageUrl,
                            isLoading = false
                        )
                    }
                } else {
                    // Fallback to default mock values if database is empty
                    _state.update {
                        it.copy(
                            name = "Dr. Alejandro Díaz",
                            email = "contacto@clinicadiaz.com",
                            phone = "+591 76543210",
                            description = "Especialista en Odontología Estética con más de 10 años de trayectoria ayudando a diseñar sonrisas saludables.",
                            imageUrl = "https://images.unsplash.com/photo-1622253692010-333f2da6031d?q=80&w=200&auto=format&fit=crop",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        viewModelScope.launch {
            when (event) {
                is ProfileEvent.NameChanged -> {
                    _state.update { it.copy(name = event.value) }
                }
                is ProfileEvent.EmailChanged -> {
                    _state.update { it.copy(email = event.value) }
                }
                is ProfileEvent.PhoneChanged -> {
                    _state.update { it.copy(phone = event.value) }
                }
                is ProfileEvent.DescriptionChanged -> {
                    _state.update { it.copy(description = event.value) }
                }
                is ProfileEvent.ImageUrlChanged -> {
                    _state.update { it.copy(imageUrl = event.value) }
                }
                ProfileEvent.ToggleEditMode -> {
                    _state.update { it.copy(isEditing = !it.isEditing) }
                }
                ProfileEvent.SaveProfile -> {
                    saveProfile()
                }
                ProfileEvent.LoadProfile -> {
                    loadUserProfile()
                }
            }
        }
    }

    private suspend fun saveProfile() {
        val currentState = _state.value
        _state.update { it.copy(isLoading = true) }
        
        val profile = UserProfile(
            name = currentState.name,
            email = currentState.email,
            phone = currentState.phone,
            description = currentState.description,
            imageUrl = currentState.imageUrl
        )
        
        val result = updateUserProfileUseCase(profile)
        _state.update { it.copy(isLoading = false, isEditing = false) }
        
        if (result.isSuccess) {
            _effect.emit(ProfileEfffect.ShowSnackbar("¡Perfil actualizado con éxito!"))
        } else {
            _effect.emit(ProfileEfffect.ShowSnackbar("Error al guardar cambios localmente."))
        }
    }
}
