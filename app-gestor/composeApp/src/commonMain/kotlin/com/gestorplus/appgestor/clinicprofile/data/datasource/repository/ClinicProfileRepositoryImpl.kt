package com.gestorplus.appgestor.clinicprofile.data.datasource.repository

import com.gestorplus.appgestor.clinicprofile.data.datasource.datasource.ClinicProfileRemoteDataSource
import com.gestorplus.appgestor.clinicprofile.data.datasource.mapper.ClinicProfileMapper
import com.gestorplus.appgestor.clinicprofile.domain.model.ClinicProfile
import com.gestorplus.appgestor.clinicprofile.domain.repository.ClinicProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ClinicProfileRepositoryImpl(
    private val remoteDataSource: ClinicProfileRemoteDataSource,
    private val mapper: ClinicProfileMapper
) : ClinicProfileRepository {
    override fun getClinicProfile(): Flow<ClinicProfile> {
        return remoteDataSource.getClinicProfile().map { mapper.toDomain(it) }
    }
}
