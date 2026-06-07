package com.gestorplus.appgestor.services_entry.domain.repository

import com.gestorplus.appgestor.services_entry.domain.model.ProfessionalDashboardModel
import kotlinx.coroutines.flow.Flow

interface ProfessionalDashboardRepository {
    fun getDashboardData(): Flow<ProfessionalDashboardModel>
}
