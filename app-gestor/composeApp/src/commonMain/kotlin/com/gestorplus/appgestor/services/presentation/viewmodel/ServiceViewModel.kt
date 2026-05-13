package com.gestorplus.appgestor.services.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.services.domain.usecase.GetServicesUseCase
import com.gestorplus.appgestor.services.presentation.state.ServiceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServiceViewModel(
    private val getServicesUseCase: GetServicesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServiceState())
    val uiState = _uiState.asStateFlow()

    init {
        loadServices()
    }

    private fun loadServices() {
        viewModelScope.launch {
            getServicesUseCase()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { error -> _uiState.update { it.copy(isLoading = false, error = error.message) } }
                .collect { services ->
                    _uiState.update { it.copy(isLoading = false, services = services) }
                }
        }
    }
}
