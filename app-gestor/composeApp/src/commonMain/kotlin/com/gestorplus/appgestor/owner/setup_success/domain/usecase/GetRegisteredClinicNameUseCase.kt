package com.gestorplus.appgestor.owner.setup_success.domain.usecase

import com.gestorplus.appgestor.owner.setup_success.domain.repository.SetupSuccessRepository

class GetRegisteredClinicNameUseCase(private val repository: SetupSuccessRepository) {
    suspend operator fun invoke(): Result<String> {
        return repository.getRegisteredClinicName()
    }
}
