package com.gestorplus.appgestor.clinicProfile.data.datasource.service

import com.gestorplus.appgestor.clinicProfile.data.datasource.dto.ClinicProfileDto
import com.gestorplus.appgestor.data.datasource.FirebaseManager
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class ClinicProfileService(private val firebaseManager: FirebaseManager) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getClinicProfile(ownerUid: String): ClinicProfileDto? {
        val data = firebaseManager.getData("clinics/$ownerUid") ?: return null
        // Convert Map to JSON string then to DTO
        val jsonString = json.encodeToString(data)
        return json.decodeFromString<ClinicProfileDto>(jsonString)
    }

    suspend fun updateClinicProfile(ownerUid: String, dto: ClinicProfileDto) {
        val jsonString = json.encodeToString(dto)
        val dataMap = json.decodeFromString<Map<String, String>>(jsonString)
        dataMap.forEach { (key, value) ->
            firebaseManager.saveData("clinics/$ownerUid/$key", value)
        }
    }
}
