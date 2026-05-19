package com.gestorplus.appgestor.profile.domain.model

data class UserProfile(
    val name: String,
    val email: String,
    val phone: String,
    val description: String,
    val imageUrl: String
)
