package com.gestorplus.appgestor.presentation.profile

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel {

    // 1) State (lo que la pantalla "muestra")
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    // 2) Effect (cosas de 1 sola vez: navegar, toast, etc.)
    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect = _effect.asSharedFlow()

    // 3) Recibir eventos desde la UI
    suspend fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.EmailChanged -> {
                _state.value = _state.value.copy(email = event.value)
            }

            is ProfileEvent.PhoneChanged -> {
                _state.value = _state.value.copy(phone = event.value)
            }

            is ProfileEvent.DescriptionChanged -> {
                _state.value = _state.value.copy(description = event.value)
            }

            ProfileEvent.OnNextClicked -> {
                _effect.emit(ProfileEffect.NavigateNext)
            }
        }
    }
}