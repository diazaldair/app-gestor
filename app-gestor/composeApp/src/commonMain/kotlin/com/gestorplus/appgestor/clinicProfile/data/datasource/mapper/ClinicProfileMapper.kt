package com.gestorplus.appgestor.clinicProfile.data.datasource.mapper

import com.gestorplus.appgestor.clinicProfile.data.datasource.dto.ClinicProfileDto
import com.gestorplus.appgestor.clinicProfile.domain.model.ClinicProfile

class ClinicProfileMapper {
    fun toDomain(dto: ClinicProfileDto): ClinicProfile {
        return ClinicProfile(
            name = dto.name ?: "",
            biography = dto.bio ?: "",
            specialties = dto.specialties ?: emptyList(),
            address = dto.address ?: "",
            mapPreviewUrl = dto.mapUrl
        )
    }

    fun toDto(model: ClinicProfile): ClinicProfileDto {
        return ClinicProfileDto(
            name = model.name,
            bio = model.biography,
            specialties = model.specialties,
            address = model.address,
            mapUrl = model.mapPreviewUrl
        )
    }
}
