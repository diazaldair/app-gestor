package com.gestorplus.appgestor.clinicprofile.data.datasource.mapper

import com.gestorplus.appgestor.clinicprofile.data.datasource.dto.ClinicProfileDto
import com.gestorplus.appgestor.clinicprofile.data.datasource.dto.ClinicServiceDto
import com.gestorplus.appgestor.clinicprofile.domain.model.ClinicProfile
import com.gestorplus.appgestor.clinicprofile.domain.model.ClinicService

class ClinicProfileMapper {
    fun toDomain(dto: ClinicProfileDto?): ClinicProfile {
        return ClinicProfile(
            name = dto?.name ?: "",
            subtitle = dto?.subtitle ?: "",
            bio = dto?.bio ?: "",
            location = dto?.location ?: "",
            specialties = dto?.specialties ?: emptyList(),
            services = dto?.services?.map { toDomain(it) } ?: emptyList()
        )
    }

    private fun toDomain(dto: ClinicServiceDto): ClinicService {
        return ClinicService(
            id = dto.id ?: "",
            name = dto.name ?: "",
            duration = dto.duration ?: "",
            price = dto.price ?: "",
            description = dto.description ?: ""
        )
    }
}
