package com.gestorplus.appgestor.clinicProfile.data.datasource.repository

import com.gestorplus.appgestor.clinicProfile.domain.model.ClinicProfile
import com.gestorplus.appgestor.clinicProfile.domain.repository.ClinicProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ClinicProfileRepositoryImpl : ClinicProfileRepository {
    override fun getClinicProfile(): Flow<ClinicProfile> {
        return flowOf(
            ClinicProfile(
                name = "SoloBook Medical Center",
                biography = "Centro especializado en medicina deportiva y rehabilitación avanzada con tecnología de vanguardia.",
                specialties = listOf("Fisioterapia", "Osteopatía"),
                address = "Av. de la Libertad 124, Madrid"
            )
        )
    }

    override suspend fun updateClinicProfile(profile: ClinicProfile) {
        // Implementación real aquí (Firebase/Room)
    }
}
