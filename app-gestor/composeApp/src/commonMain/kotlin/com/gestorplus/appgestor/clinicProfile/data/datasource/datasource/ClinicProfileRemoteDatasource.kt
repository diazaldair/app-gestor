package com.gestorplus.appgestor.clinicProfile.data.datasource.datasource

import com.gestorplus.appgestor.clinicProfile.data.datasource.dto.ClinicProfileDto
import com.gestorplus.appgestor.clinicProfile.data.datasource.service.ClinicProfileService

class ClinicProfileRemoteDatasource(private val service: ClinicProfileService) {
    suspend fun fetchProfile(): ClinicProfileDto = service.getClinicProfile()
    suspend fun updateProfile(dto: ClinicProfileDto) = service.updateClinicProfile(dto)
}
