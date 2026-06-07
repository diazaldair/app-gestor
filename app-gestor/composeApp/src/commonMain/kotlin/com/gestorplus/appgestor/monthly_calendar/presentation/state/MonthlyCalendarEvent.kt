package com.gestorplus.appgestor.monthly_calendar.presentation.state

sealed interface MonthlyCalendarEvent {
    data object OnSettingsClicked : MonthlyCalendarEvent
}
