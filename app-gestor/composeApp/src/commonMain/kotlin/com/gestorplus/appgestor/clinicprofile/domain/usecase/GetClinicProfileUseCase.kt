package com.gestorplus.appgestor.clinicprofile.domain.usecase

import com.gestorplus.appgestor.clinicprofile.domain.model.ClinicProfile
import com.gestorplus.appgestor.clinicprofile.domain.repository.ClinicProfileRepository
import kotlinx.coroutines.flow.Flow

class GetClinicProfileUseCase(private val repository: ClinicProfileRepository) {
    operator fun invoke(): Flow<ClinicProfile> {
        return repository.getClinicProfile()
    }
}
