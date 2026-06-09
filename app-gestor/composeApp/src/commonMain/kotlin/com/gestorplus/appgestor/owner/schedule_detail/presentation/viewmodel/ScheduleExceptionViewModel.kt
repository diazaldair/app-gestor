package com.gestorplus.appgestor.owner.schedule_detail.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.owner.schedule.domain.model.ScheduleException
import com.gestorplus.appgestor.owner.schedule.domain.usecase.DeleteExceptionUseCase
import com.gestorplus.appgestor.owner.schedule.domain.usecase.GetExceptionsUseCase
import com.gestorplus.appgestor.owner.schedule.domain.usecase.SaveExceptionUseCase
import com.gestorplus.appgestor.owner.schedule_detail.presentation.state.ScheduleExceptionEffect
import com.gestorplus.appgestor.owner.schedule_detail.presentation.state.ScheduleExceptionEvent
import com.gestorplus.appgestor.owner.schedule_detail.presentation.state.ScheduleExceptionUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScheduleExceptionViewModel(
    private val getExceptionsUseCase: GetExceptionsUseCase,
    private val saveExceptionUseCase: SaveExceptionUseCase,
    private val deleteExceptionUseCase: DeleteExceptionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ScheduleExceptionUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ScheduleExceptionEffect>()
    val effect = _effect.asSharedFlow()

    private var originalDate: String? = null

    fun onEvent(event: ScheduleExceptionEvent) {
        when (event) {
            is ScheduleExceptionEvent.DateSelected -> {
                originalDate = event.date
                loadExceptionForDate(event.date)
            }
            is ScheduleExceptionEvent.IsOpenChanged -> _state.update { it.copy(exception = it.exception.copy(isOpen = event.isOpen)) }
            is ScheduleExceptionEvent.CustomMorningStartChanged -> _state.update { it.copy(exception = it.exception.copy(customMorningStart = event.value)) }
            is ScheduleExceptionEvent.CustomMorningEndChanged -> _state.update { it.copy(exception = it.exception.copy(customMorningEnd = event.value)) }
            is ScheduleExceptionEvent.CustomAfternoonStartChanged -> _state.update { it.copy(exception = it.exception.copy(customAfternoonStart = event.value)) }
            is ScheduleExceptionEvent.CustomAfternoonEndChanged -> _state.update { it.copy(exception = it.exception.copy(customAfternoonEnd = event.value)) }
            is ScheduleExceptionEvent.ReasonChanged -> _state.update { it.copy(exception = it.exception.copy(reason = event.reason)) }
            ScheduleExceptionEvent.QuickFullDay -> {
                _state.update {
                    it.copy(
                        exception = it.exception.copy(
                            customMorningStart = "00:00",
                            customMorningEnd = "23:59",
                            customAfternoonStart = null,
                            customAfternoonEnd = null
                        )
                    )
                }
            }
            ScheduleExceptionEvent.QuickLateStart -> {
                _state.update {
                    it.copy(
                        exception = it.exception.copy(
                            customMorningStart = "10:00",
                            customMorningEnd = it.exception.customMorningEnd ?: "13:00"
                        )
                    )
                }
            }
            ScheduleExceptionEvent.QuickEarlyClose -> {
                _state.update {
                    it.copy(
                        exception = it.exception.copy(
                            customAfternoonEnd = "17:00"
                        )
                    )
                }
            }
            ScheduleExceptionEvent.SaveException -> saveException()
            ScheduleExceptionEvent.DeleteException -> deleteException()
            ScheduleExceptionEvent.LoadInitialData -> loadInitialData()
        }
    }

    private fun loadExceptionForDate(date: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getExceptionsUseCase().collect { exceptions ->
                val existing = exceptions.find { it.date == date }
                if (existing != null) {
                    _state.update { it.copy(exception = existing, isLoading = false) }
                } else {
                    _state.update {
                        it.copy(
                            exception = ScheduleException(id = "", date = date, isOpen = true),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun saveException() {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            val result = saveExceptionUseCase(_state.value.exception)
            _state.update { it.copy(isSaving = false) }
            result.fold(
                onSuccess = {
                    _effect.emit(ScheduleExceptionEffect.ShowSnackbar("Excepción guardada"))
                    _effect.emit(ScheduleExceptionEffect.NavigateBack)
                },
                onFailure = { error ->
                    _effect.emit(ScheduleExceptionEffect.ShowSnackbar(error.message ?: "Error al guardar"))
                }
            )
        }
    }

    private fun deleteException() {
        viewModelScope.launch {
            val exceptionId = _state.value.exception.id
            if (exceptionId.isBlank()) {
                _effect.emit(ScheduleExceptionEffect.ShowSnackbar("No hay excepción para eliminar"))
                return@launch
            }
            val result = deleteExceptionUseCase(exceptionId)
            result.fold(
                onSuccess = {
                    _effect.emit(ScheduleExceptionEffect.ShowSnackbar("Excepción eliminada"))
                    _effect.emit(ScheduleExceptionEffect.NavigateBack)
                },
                onFailure = { error ->
                    _effect.emit(ScheduleExceptionEffect.ShowSnackbar(error.message ?: "Error al eliminar"))
                }
            )
        }
    }

    private fun loadInitialData() { }
}