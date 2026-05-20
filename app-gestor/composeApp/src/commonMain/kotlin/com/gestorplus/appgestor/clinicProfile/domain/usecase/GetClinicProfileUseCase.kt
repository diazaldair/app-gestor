package com.gestorplus.appgestor.clinicProfile.domain.usecase

import com.gestorplus.appgestor.clinicProfile.domain.repository.ClinicProfileRepository

class GetClinicProfileUseCase(private val repository: ClinicProfileRepository) {
    operator fun invoke() = repository.getClinicProfile()
}
