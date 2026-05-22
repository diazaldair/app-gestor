package com.gestorplus.appgestor.owner.setup_schedule.domain.model

data class WorkspaceSchedule(
    val workingDays: List<String>,       // ["L", "M", "X", "J", "V"]
    val morningStart: String,            // "08:00"
    val morningEnd: String,              // "13:00"
    val afternoonStart: String,          // "15:00"
    val afternoonEnd: String             // "20:00"
)
