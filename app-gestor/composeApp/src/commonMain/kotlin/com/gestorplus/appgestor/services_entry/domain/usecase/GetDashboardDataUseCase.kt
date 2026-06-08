package com.gestorplus.appgestor.services_entry.domain.usecase

import com.gestorplus.appgestor.services_entry.domain.model.ProfessionalDashboardModel
import com.gestorplus.appgestor.services_entry.domain.repository.ServicesEntryRepository
import kotlinx.coroutines.flow.Flow

class GetDashboardDataUseCase(private val repository: ServicesEntryRepository) {
    operator fun invoke(): Flow<ProfessionalDashboardModel> = repository.getDashboardData()
}
