package com.gestorplus.appgestor.clinicProfile.domain.repository

import com.gestorplus.appgestor.clinicProfile.domain.model.ClinicProfile
import kotlinx.coroutines.flow.Flow

interface ClinicProfileRepository {
    fun getClinicProfile(): Flow<ClinicProfile>
    suspend fun updateClinicProfile(profile: ClinicProfile)
}
