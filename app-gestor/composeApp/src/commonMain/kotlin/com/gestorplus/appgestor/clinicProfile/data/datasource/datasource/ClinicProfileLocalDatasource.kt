package com.gestorplus.appgestor.clinicProfile.data.datasource.datasource

import com.gestorplus.appgestor.clinicProfile.data.local.dao.ClinicProfileDao
import com.gestorplus.appgestor.clinicProfile.data.local.entity.ClinicProfileEntity
import com.gestorplus.appgestor.clinicProfile.data.datasource.dto.ClinicProfileDto
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class ClinicProfileLocalDatasource(
    private val clinicProfileDao: ClinicProfileDao
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getCachedProfile(): ClinicProfileDto? {
        val entity = clinicProfileDao.getProfile().firstOrNull() ?: return null
        return ClinicProfileDto(
            name = entity.name,
            bio = entity.bio,
            specialties = try {
                json.decodeFromString<List<String>>(entity.specialtiesJson)
            } catch (e: Exception) {
                emptyList()
            },
            address = entity.address,
            mapUrl = entity.mapUrl
        )
    }

    suspend fun saveProfile(dto: ClinicProfileDto) {
        val entity = ClinicProfileEntity(
            name = dto.name ?: "",
            bio = dto.bio ?: "",
            specialtiesJson = json.encodeToString(dto.specialties ?: emptyList<String>()),
            address = dto.address ?: "",
            mapUrl = dto.mapUrl
        )
        clinicProfileDao.insertProfile(entity)
    }
}
