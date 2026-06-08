package com.gestorplus.appgestor.clinicProfile.domain.usecase

import com.gestorplus.appgestor.clinicProfile.domain.model.ClinicProfile
import com.gestorplus.appgestor.clinicProfile.domain.repository.ClinicProfileRepository
import kotlinx.coroutines.flow.Flow

class GetClinicProfileUseCase(private val repository: ClinicProfileRepository) {
    operator fun invoke(): Flow<ClinicProfile> = repository.getClinicProfile()
}
