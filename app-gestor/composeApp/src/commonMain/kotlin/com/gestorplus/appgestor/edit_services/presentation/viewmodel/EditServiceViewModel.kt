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

class EditServiceViewModel(
    private val repository: ServiceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditServiceUiState())
    val uiState = _uiState.asStateFlow()

    private var currentServiceId: String? = null

    fun onEvent(event: EditServiceEvent) {
        when (event) {
            is EditServiceEvent.NameChanged -> _uiState.update { it.copy(name = event.name) }
            is EditServiceEvent.CategoryChanged -> _uiState.update { it.copy(category = event.category) }
            is EditServiceEvent.DescriptionChanged -> _uiState.update { it.copy(description = event.description) }
            is EditServiceEvent.PriceChanged -> _uiState.update { it.copy(price = event.price) }
            EditServiceEvent.SaveService -> saveChanges()
            else -> {}
        }
    }

    private fun saveChanges() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            // Aquí iría la lógica real de guardado en el repositorio
            // Por ahora simulamos un éxito
            kotlinx.coroutines.delay(1000)
            _uiState.update { it.copy(isSaving = false) }
        }
    }

    fun loadService(id: String) {
        currentServiceId = id
        // Aquí cargarías el servicio desde el repositorio
        // Simulamos carga de datos iniciales
        _uiState.update { it.copy(
            name = "Consulta Médica General",
            category = "Medicina General",
            description = "Consulta integral para diagnóstico.",
            price = "50.00"
        )}
    }
}
