package com.gestorplus.appgestor.booking.presentation.state

import com.gestorplus.appgestor.clinicProfile.domain.model.Clinic
import com.gestorplus.appgestor.clinic_detail.domain.model.ClinicService

data class BookingConfirmationState(
    val clinic: Clinic? = null,
    val service: ClinicService? = null,
    val date: Int = 0,
    val month: String = "",
    val timeSlot: String = "",
    val notes: String = "",
    val isLoading: Boolean = false,
    val isConfirmed: Boolean = false,
    val error: String? = null
)
