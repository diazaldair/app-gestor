package com.gestorplus.appgestor.monthly_calendar.domain.model

data class MonthlyCalendarModel(
    val monthName: String,
    val year: Int,
    val days: List<CalendarDayModel>
)

data class CalendarDayModel(
    val day: Int,
    val hasAppointments: Boolean,
    val isSelected: Boolean,
    val isCurrentMonth: Boolean,
    val status: String? = null // CONFIRMED, PENDING, BLOCKED
)
