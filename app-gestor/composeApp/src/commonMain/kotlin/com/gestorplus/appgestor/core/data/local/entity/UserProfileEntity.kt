package com.gestorplus.appgestor.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: String = "owner_profile",
    val name: String,
    val email: String,
    val phone: String,
    val description: String,
    val imageUrl: String
)
