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
        // 1. Emitir datos desde la caché local si existen
        val cachedDto = localDatasource.getCachedProfile()
        if (cachedDto != null) {
            emit(mapper.toDomain(cachedDto))
        }

        try {
            // 2. Intentar obtener datos desde el servidor
            val remoteDto = remoteDatasource.fetchProfile()
            if (remoteDto != null) {
                // 3. Guardar en la caché local
                localDatasource.saveProfile(remoteDto)
                // 4. Emitir los datos actualizados
                emit(mapper.toDomain(remoteDto))
            } else if (cachedDto == null) {
                // Si no hay nada en caché ni en remoto, emitir perfil vacío
                emit(ClinicProfile())
            }
        } catch (e: Exception) {
            // Si falla el remoto y no hay caché, asegurar que se emite algo
            if (cachedDto == null) {
                emit(ClinicProfile())
            }
        }
    }

    override suspend fun updateClinicProfile(profile: ClinicProfile) {
        val dto = mapper.toDto(profile)
        // 1. Actualizar en el servidor
        remoteDatasource.updateProfile(dto)
        // 2. Mantener la caché local sincronizada
        localDatasource.saveProfile(dto)
    }
}
