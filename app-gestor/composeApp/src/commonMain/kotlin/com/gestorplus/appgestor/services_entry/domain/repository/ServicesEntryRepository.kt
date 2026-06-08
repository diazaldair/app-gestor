package com.gestorplus.appgestor.services_entry.domain.repository

import com.gestorplus.appgestor.services_entry.domain.model.ProfessionalDashboardModel
import kotlinx.coroutines.flow.Flow

interface ServicesEntryRepository {
    fun getDashboardData(): Flow<ProfessionalDashboardModel>
}
