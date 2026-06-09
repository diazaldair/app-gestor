package com.gestorplus.appgestor.edit_services.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.edit_services.presentation.state.EditServiceEvent
import com.gestorplus.appgestor.edit_services.presentation.state.EditServiceUiState
import com.gestorplus.appgestor.services.domain.model.ServiceModel
import com.gestorplus.appgestor.services.domain.repository.ServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class EditServiceViewModel(
    private val repository: ServiceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditServiceUiState())
    val uiState = _uiState.asStateFlow()

    private var currentService: ServiceModel? = null

    fun onEvent(event: EditServiceEvent, onSuccess: () -> Unit = {}) {
        when (event) {
            is EditServiceEvent.NameChanged -> _uiState.update { it.copy(name = event.name) }
            is EditServiceEvent.CategoryChanged -> _uiState.update { it.copy(category = event.category) }
            is EditServiceEvent.DescriptionChanged -> _uiState.update { it.copy(description = event.description) }
            is EditServiceEvent.PriceChanged -> _uiState.update { it.copy(price = event.price) }
            is EditServiceEvent.DurationChanged -> _uiState.update { it.copy(hours = event.hours, minutes = event.minutes) }
            EditServiceEvent.SaveService -> saveChanges(onSuccess)
            EditServiceEvent.DeleteService -> deleteService(onSuccess)
        }
    }

    private fun saveChanges(onSuccess: () -> Unit) {
        val state = _uiState.value
        val totalMinutes = (state.hours * 60) + state.minutes
        
        val serviceToSave = (currentService ?: ServiceModel()).copy(
            name = state.name,
            category = state.category,
            description = state.description,
            price = state.price.replace(",", ".").toDoubleOrNull() ?: 0.0,
            durationMinutes = if (totalMinutes > 0) totalMinutes else 30
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            repository.saveService(serviceToSave).onSuccess {
                delay(500)
                _uiState.update { it.copy(isSaving = false) }
                onSuccess()
            }.onFailure {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun deleteService(onSuccess: () -> Unit) {
        val serviceId = currentService?.id ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true) }
            repository.deleteService(serviceId)
            delay(500)
            _uiState.update { it.copy(isDeleting = false) }
            onSuccess()
        }
    }

    fun loadService(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val service = repository.getServiceById(id)
            
            if (service != null) {
                updateStateFromService(service)
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun updateStateFromService(service: ServiceModel) {
        currentService = service
        val hours = service.durationMinutes / 60
        val minutes = service.durationMinutes % 60
        
        _uiState.update { it.copy(
            name = service.name,
            category = service.category,
            description = service.description,
            price = service.price.toString().replace(".", ","),
            currency = service.currency,
            hours = hours,
            minutes = minutes
        )}
    }
}
