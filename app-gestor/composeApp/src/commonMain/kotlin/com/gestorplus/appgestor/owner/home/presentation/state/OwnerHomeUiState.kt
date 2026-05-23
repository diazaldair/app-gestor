package com.gestorplus.appgestor.owner.home.presentation.state

import androidx.compose.runtime.Immutable
import com.gestorplus.appgestor.owner.home.domain.model.Appointment

@Immutable
data class OwnerHomeUiState(
    val isLoading: Boolean = true,
    val doctorName: String = "",
    val totalAppointments: Int = 0,
    val pendingAppointments: Int = 0,
    val nextAppointment: Appointment? = null,
    val restOfDayAppointments: List<Appointment> = emptyList(),
    val errorMessage: String? = null
)
