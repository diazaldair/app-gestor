package com.gestorplus.appgestor.clinicProfile.data.datasource.service

import com.gestorplus.appgestor.clinicProfile.data.datasource.dto.ClinicProfileDto

class ClinicProfileService {
    suspend fun getClinicProfile(): ClinicProfileDto {
        // Mock data for now
        return ClinicProfileDto(
            name = "SoloBook Medical Center",
            bio = "Centro especializado en medicina deportiva y rehabilitación avanzada con tecnología de vanguardia.",
            specialties = listOf("Fisioterapia", "Osteopatía"),
            address = "Av. de la Libertad 124, Madrid"
        )
    }

    suspend fun updateClinicProfile(dto: ClinicProfileDto) {
        // Minimal implementation
    }
}
