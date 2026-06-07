package com.gestorplus.appgestor.services.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.gestorplus.appgestor.services.presentation.state.ServicesCatalogUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ServicesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ServicesCatalogUiState())
    val uiState = _uiState.asStateFlow()
}
