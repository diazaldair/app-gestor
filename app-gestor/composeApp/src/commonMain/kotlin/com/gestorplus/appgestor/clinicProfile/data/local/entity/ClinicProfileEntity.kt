package com.gestorplus.appgestor.clinicProfile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clinic_profile")
data class ClinicProfileEntity(
    @PrimaryKey
    val id: String = "single_clinic_profile",
    val name: String,
    val bio: String,
    val specialtiesJson: String,
    val address: String,
    val locationUrl: String?,
    val latitude: Double? = null,
    val longitude: Double? = null
)
