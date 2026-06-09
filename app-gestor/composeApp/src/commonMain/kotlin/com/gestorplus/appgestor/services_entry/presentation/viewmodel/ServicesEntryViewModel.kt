package com.gestorplus.appgestor.services_entry.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.gestorplus.appgestor.core.persistence.LocalPreferences
import com.gestorplus.appgestor.services_entry.presentation.state.ServicesEntryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ServicesEntryViewModel(
    private val localPreferences: LocalPreferences
) : ViewModel() {
    private val _uiState = MutableStateFlow(ServicesEntryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProfessionalName()
    }

    private fun loadProfessionalName() {
        val name = localPreferences.getString("registered_professional_name", "Profesional")
        _uiState.update { it.copy(professionalName = "Dr. $name") }
    }
}
