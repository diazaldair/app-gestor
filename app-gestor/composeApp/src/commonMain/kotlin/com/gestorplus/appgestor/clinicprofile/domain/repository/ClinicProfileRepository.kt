package com.gestorplus.appgestor.clinicprofile.domain.repository

import com.gestorplus.appgestor.clinicprofile.domain.model.ClinicProfile
import kotlinx.coroutines.flow.Flow

interface ClinicProfileRepository {
    fun getClinicProfile(): Flow<ClinicProfile>
}
