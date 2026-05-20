package com.gestorplus.appgestor.notifications.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.notifications.domain.usecase.GetNotificationsUseCase
import com.gestorplus.appgestor.notifications.presentation.state.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<NotificationsEfffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getNotificationsUseCase().collect { list ->
                _uiState.update { it.copy(notifications = list, isLoading = false) }
            }
        }
    }

    fun onEvent(event: NotificationsEvent) {
        when (event) {
            is NotificationsEvent.OnFilterSelected -> {
                _uiState.update { it.copy(selectedFilter = event.filter) }
            }
            is NotificationsEvent.OnAcceptAppointment -> {
                viewModelScope.launch {
                    _effect.emit(NotificationsEfffect.ShowToast("Cita aceptada"))
                }
            }
            is NotificationsEvent.OnDeclineAppointment -> {
                viewModelScope.launch {
                    _effect.emit(NotificationsEfffect.ShowToast("Cita declinada"))
                }
            }
        }
    }
}
