package com.gestorplus.appgestor.owner.domain.usecase

import com.gestorplus.appgestor.owner.domain.repository.OwnerRepository

class GetFirebaseLogsUseCase(private val repository: OwnerRepository) {
    suspend operator fun invoke(path: String): List<String> {
        return repository.getFirebaseLogs(path)
    }
}
