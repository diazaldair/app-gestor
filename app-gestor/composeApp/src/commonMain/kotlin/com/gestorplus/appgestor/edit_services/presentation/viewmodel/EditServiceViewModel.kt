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
            EditServiceEvent.SaveService -> saveChanges(onSuccess)
            else -> {}
        }
    }

    private fun saveChanges(onSuccess: () -> Unit) {
        val state = _uiState.value
        val serviceToSave = (currentService ?: ServiceModel(id = "8")).copy(
            name = state.name,
            category = state.category,
            description = state.description,
            price = state.price.toDoubleOrNull() ?: 0.0
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            repository.saveService(serviceToSave).onSuccess {
                delay(500) // Feedback visual
                _uiState.update { it.copy(isSaving = false) }
                onSuccess()
            }.onFailure {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    fun loadService(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val service = repository.getServiceById(id)
            
            if (service != null) {
                updateStateFromService(service)
            } else {
                // Crear un servicio por defecto para el ID 8 si no existe
                val defaultService = ServiceModel(
                    id = id,
                    name = "Servicio Nuevo $id",
                    category = "General",
                    description = "Descripción del servicio...",
                    price = 100.0
                )
                currentService = defaultService
                updateStateFromService(defaultService)
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun updateStateFromService(service: ServiceModel) {
        currentService = service
        _uiState.update { it.copy(
            name = service.name,
            category = service.category,
            description = service.description,
            price = service.price.toString(),
            currency = service.currency
        )}
    }
}
