package com.gestorplus.appgestor.clinicProfile.domain.usecase

import com.gestorplus.appgestor.clinicProfile.domain.model.ClinicProfile
import com.gestorplus.appgestor.clinicProfile.domain.repository.ClinicProfileRepository

class UpdateClinicProfileUseCase(private val repository: ClinicProfileRepository) {
    suspend operator fun invoke(profile: ClinicProfile) = repository.updateClinicProfile(profile)
}
