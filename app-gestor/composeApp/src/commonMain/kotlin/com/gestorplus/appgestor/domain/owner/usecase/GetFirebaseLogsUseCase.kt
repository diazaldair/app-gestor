package com.gestorplus.appgestor.domain.owner.usecase

import com.gestorplus.appgestor.domain.owner.repository.OwnerRepository

class GetFirebaseLogsUseCase(private val repository: OwnerRepository) {
    suspend operator fun invoke(path: String): List<String> {
        return repository.getFirebaseLogs(path)
    }
}
