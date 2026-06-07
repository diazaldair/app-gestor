package com.gestorplus.appgestor.monthly_calendar.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.gestorplus.appgestor.monthly_calendar.presentation.state.MonthlyCalendarUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MonthlyCalendarViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MonthlyCalendarUiState())
    val uiState = _uiState.asStateFlow()
}
