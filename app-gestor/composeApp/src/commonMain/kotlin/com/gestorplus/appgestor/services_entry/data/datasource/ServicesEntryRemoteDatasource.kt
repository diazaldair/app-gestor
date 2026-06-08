package com.gestorplus.appgestor.services_entry.data.datasource

import com.gestorplus.appgestor.services_entry.data.service.ServicesEntryService
import com.gestorplus.appgestor.services_entry.domain.model.ProfessionalDashboardModel

class ServicesEntryRemoteDatasource(
    private val service: ServicesEntryService
) {
    suspend fun getDashboardData(): ProfessionalDashboardModel {
        return service.fetchDashboardData()
    }
}
