package com.gestorplus.appgestor.owner.dashboard.domain.usecase

import com.gestorplus.appgestor.owner.dashboard.domain.repository.DashboardRepository

class GetFirebaseLogsUseCase(private val repository: DashboardRepository) {
    suspend operator fun invoke(path: String): List<String> {
        return repository.getFirebaseLogs(path)
    }
}
