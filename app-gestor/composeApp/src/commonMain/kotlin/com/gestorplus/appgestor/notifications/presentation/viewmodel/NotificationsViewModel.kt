package com.gestorplus.appgestor.notifications.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.notifications.domain.usecase.AcceptAppointmentUseCase
import com.gestorplus.appgestor.notifications.domain.usecase.DeclineAppointmentUseCase
import com.gestorplus.appgestor.notifications.domain.usecase.GetNotificationsUseCase
import com.gestorplus.appgestor.notifications.presentation.state.NotificationsEfffect
import com.gestorplus.appgestor.notifications.presentation.state.NotificationsEvent
import com.gestorplus.appgestor.notifications.presentation.state.NotificationsUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val acceptAppointmentUseCase: AcceptAppointmentUseCase,
    private val declineAppointmentUseCase: DeclineAppointmentUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationsUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<NotificationsEfffect>()
    val effect: SharedFlow<NotificationsEfffect> = _effect.asSharedFlow()

    init {
        observeNotifications()
    }

    private fun observeNotifications() {
        viewModelScope.launch {
            getNotificationsUseCase().collectLatest { list ->
                // Debug: loaded notifications count
                val loadedCount = list.size
                val currentFilter = _state.value.filter
                val filteredCount = when (currentFilter) {
                    com.gestorplus.appgestor.notifications.presentation.state.NotificationFilter.All -> loadedCount
                    com.gestorplus.appgestor.notifications.presentation.state.NotificationFilter.Appointments -> list.count { it.type == com.gestorplus.appgestor.notifications.domain.model.NotificationType.APPOINTMENT_REQUEST }
                    com.gestorplus.appgestor.notifications.presentation.state.NotificationFilter.Messages -> list.count { it.type == com.gestorplus.appgestor.notifications.domain.model.NotificationType.REMINDER }
                    com.gestorplus.appgestor.notifications.presentation.state.NotificationFilter.System -> list.count { it.type == com.gestorplus.appgestor.notifications.domain.model.NotificationType.SYSTEM_UPDATE }
                }
                println("NotificationsViewModel: loadedCount=$loadedCount, filter=$currentFilter, filteredCount=$filteredCount")

                _state.value = _state.value.copy(isLoading = false, notifications = list, error = null)
            }
        }
    }

    fun onEvent(event: NotificationsEvent) {
        when (event) {
            is NotificationsEvent.Accept -> handleAccept(event.id)
            is NotificationsEvent.Decline -> handleDecline(event.id)
            is NotificationsEvent.MarkRead -> handleMarkRead(event.id)
            is NotificationsEvent.ChangeFilter -> {
                _state.value = _state.value.copy(filter = event.filter)
                // Recompute filtered count for debug
                val list = _state.value.notifications
                val currentFilter = event.filter
                val afterFilter = when (currentFilter) {
                    com.gestorplus.appgestor.notifications.presentation.state.NotificationFilter.All -> list.size
                    com.gestorplus.appgestor.notifications.presentation.state.NotificationFilter.Appointments -> list.count { it.type == com.gestorplus.appgestor.notifications.domain.model.NotificationType.APPOINTMENT_REQUEST }
                    com.gestorplus.appgestor.notifications.presentation.state.NotificationFilter.Messages -> list.count { it.type == com.gestorplus.appgestor.notifications.domain.model.NotificationType.REMINDER }
                    com.gestorplus.appgestor.notifications.presentation.state.NotificationFilter.System -> list.count { it.type == com.gestorplus.appgestor.notifications.domain.model.NotificationType.SYSTEM_UPDATE }
                }
                println("NotificationsViewModel: filter changed to=$currentFilter, notifications after filter=$afterFilter")
            }
            NotificationsEvent.Refresh -> observeNotifications()
        }
    }

    private fun handleAccept(id: String) {
        viewModelScope.launch {
            try {
                acceptAppointmentUseCase(id)
                _effect.emit(NotificationsEfffect.ShowMessage("Solicitud aceptada"))
            } catch (t: Throwable) {
                _effect.emit(NotificationsEfffect.ShowMessage("Error al aceptar"))
            }
        }
    }

    private fun handleDecline(id: String) {
        viewModelScope.launch {
            try {
                declineAppointmentUseCase(id)
                _effect.emit(NotificationsEfffect.ShowMessage("Solicitud rechazada"))
            } catch (t: Throwable) {
                _effect.emit(NotificationsEfffect.ShowMessage("Error al rechazar"))
            }
        }
    }

    private fun handleMarkRead(id: String) {
        viewModelScope.launch {
            try {
                _effect.emit(NotificationsEfffect.ShowMessage("Marcado como leído"))
            } catch (_: Throwable) {
            }
        }
    }
}
