package com.gestorplus.appgestor.clinicProfile.data.datasource.service

import com.gestorplus.appgestor.clinicProfile.data.datasource.dto.ClinicProfileDto

class ClinicProfileService(private val client: Any) {
    suspend fun getClinicProfile(): ClinicProfileDto {
        return ClinicProfileDto()
    }
    
    suspend fun updateClinicProfile(dto: ClinicProfileDto) {
        // Lógica de red
    }
}
