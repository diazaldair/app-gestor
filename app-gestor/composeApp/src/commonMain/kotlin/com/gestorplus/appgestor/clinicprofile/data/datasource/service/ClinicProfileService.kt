package com.gestorplus.appgestor.clinicprofile.data.datasource.service

import com.gestorplus.appgestor.clinicprofile.data.datasource.dto.ClinicProfileDto
import kotlinx.coroutines.flow.Flow

/**
 * Interface for API or Firebase service calls.
 */
interface ClinicProfileService {
    fun fetchClinicProfile(): Flow<ClinicProfileDto>
}
