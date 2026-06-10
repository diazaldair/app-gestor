package com.gestorplus.appgestor.clinicProfile.data.datasource.service

import com.gestorplus.appgestor.clinicProfile.data.datasource.dto.ClinicProfileDto
import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class ClinicProfileService(private val firebaseManager: FirebaseManager) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getClinicProfile(ownerUid: String): ClinicProfileDto? {
        val data = firebaseManager.getData("clinics/$ownerUid") ?: return null
        return try {
            val jsonString = json.encodeToString(data)
            json.decodeFromString<ClinicProfileDto>(jsonString)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateClinicProfile(ownerUid: String, dto: ClinicProfileDto) {
        dto.name?.let { firebaseManager.saveData("clinics/$ownerUid/name", it) }
        dto.bio?.let { firebaseManager.saveData("clinics/$ownerUid/bio", it) }
        dto.address?.let { firebaseManager.saveData("clinics/$ownerUid/address", it) }
        dto.locationUrl?.let { firebaseManager.saveData("clinics/$ownerUid/locationUrl", it) }
        dto.specialties?.let { 
            firebaseManager.saveData("clinics/$ownerUid/specialties", json.encodeToString(it))
        }
    }
}
