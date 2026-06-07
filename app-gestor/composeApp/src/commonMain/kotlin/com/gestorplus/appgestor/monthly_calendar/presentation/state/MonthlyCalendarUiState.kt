package com.gestorplus.appgestor.monthly_calendar.presentation.state

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@Immutable
data class MonthlyCalendarUiState(
    val selectedDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val appointments: List<CalendarAppointment> = emptyList(),
    val isLoading: Boolean = false
)

data class CalendarAppointment(
    val id: String,
    val time: String,
    val title: String,
    val patientName: String,
    val duration: String,
    val status: String // CONFIRMED, PENDING, BLOCKED
)
