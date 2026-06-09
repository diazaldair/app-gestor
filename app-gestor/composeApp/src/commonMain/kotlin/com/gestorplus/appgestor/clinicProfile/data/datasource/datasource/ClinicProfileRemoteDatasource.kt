package com.gestorplus.appgestor.clinicProfile.data.datasource.datasource

import com.gestorplus.appgestor.auth.domain.repository.AuthRepository
import com.gestorplus.appgestor.clinicProfile.data.datasource.dto.ClinicProfileDto
import com.gestorplus.appgestor.clinicProfile.data.datasource.service.ClinicProfileService
import kotlinx.coroutines.flow.firstOrNull

class ClinicProfileRemoteDatasource(
    private val service: ClinicProfileService,
    private val authRepository: AuthRepository
) {
    suspend fun fetchProfile(): ClinicProfileDto? {
        val uid = authRepository.getActiveSession().firstOrNull()?.userId ?: return null
        return service.getClinicProfile(uid)
    }

    suspend fun updateProfile(dto: ClinicProfileDto) {
        val uid = authRepository.getActiveSession().firstOrNull()?.userId ?: return
        service.updateClinicProfile(uid, dto)
    }
}
