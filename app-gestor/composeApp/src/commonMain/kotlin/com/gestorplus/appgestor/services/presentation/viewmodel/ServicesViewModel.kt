package com.gestorplus.appgestor.services.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.services.domain.usecase.GetServicesUseCase
import com.gestorplus.appgestor.services.presentation.state.ServicesCatalogUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ServicesViewModel(
    private val getServicesUseCase: GetServicesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ServicesCatalogUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadServices()
    }

    private fun loadServices() {
        getServicesUseCase()
            .onEach { services ->
                _uiState.update { it.copy(services = services) }
            }
            .launchIn(viewModelScope)
    }
}
