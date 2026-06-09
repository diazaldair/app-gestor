package com.gestorplus.appgestor.clinicProfile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clinics")
data class ClinicEntity(
    @PrimaryKey val id: String,
    val name: String,
    val address: String,
    val specialtiesJson: String,
    val imageUrl: String?,
    val isOpen: Boolean,
    val description: String
)
