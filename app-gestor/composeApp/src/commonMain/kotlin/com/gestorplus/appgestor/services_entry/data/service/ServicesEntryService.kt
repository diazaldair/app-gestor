package com.gestorplus.appgestor.services_entry.data.service

import com.gestorplus.appgestor.services_entry.domain.model.ProfessionalDashboardModel
import com.gestorplus.appgestor.services_entry.domain.model.NextAppointment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ServicesEntryService {
    // Simulación de Firebase Realtime Database
    suspend fun fetchDashboardData(): ProfessionalDashboardModel {
        return ProfessionalDashboardModel(
            professionalName = "Dr. Castro",
            appointmentsCount = 8,
            pendingCount = 3,
            nextAppointment = NextAppointment(
                patientName = "Mariana Flores",
                serviceName = "Check-up General",
                time = "09:30 AM",
                timeRemaining = "EN 15 MIN"
            )
        )
    }
}
