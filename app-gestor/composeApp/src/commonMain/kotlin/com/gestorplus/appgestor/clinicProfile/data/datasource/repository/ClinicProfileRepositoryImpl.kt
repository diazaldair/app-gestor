package com.gestorplus.appgestor.clinicProfile.data.datasource.repository

import com.gestorplus.appgestor.clinicProfile.data.datasource.datasource.ClinicProfileLocalDatasource
import com.gestorplus.appgestor.clinicProfile.data.datasource.datasource.ClinicProfileRemoteDatasource
import com.gestorplus.appgestor.clinicProfile.data.datasource.mapper.ClinicProfileMapper
import com.gestorplus.appgestor.clinicProfile.domain.model.ClinicProfile
import com.gestorplus.appgestor.clinicProfile.domain.repository.ClinicProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ClinicProfileRepositoryImpl(
    private val remoteDatasource: ClinicProfileRemoteDatasource,
    private val localDatasource: ClinicProfileLocalDatasource,
    private val mapper: ClinicProfileMapper
) : ClinicProfileRepository {

    override fun getClinicProfile(): Flow<ClinicProfile> = flow {
        // Offline-First strategy: Mocking flow for now
        val remoteDto = remoteDatasource.fetchProfile()
        emit(mapper.toDomain(remoteDto))
    }

    override suspend fun updateClinicProfile(profile: ClinicProfile) {
        remoteDatasource.updateProfile(mapper.toDto(profile))
    }
}
