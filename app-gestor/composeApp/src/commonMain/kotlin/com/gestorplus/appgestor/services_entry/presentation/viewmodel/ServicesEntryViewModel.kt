package com.gestorplus.appgestor.services_entry.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.gestorplus.appgestor.services_entry.presentation.state.ServicesEntryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ServicesEntryViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ServicesEntryUiState())
    val uiState = _uiState.asStateFlow()
}
