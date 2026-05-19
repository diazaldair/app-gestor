package com.gestorplus.appgestor.auth.domain.model

data class UserSession(
    val userId: String,
    val email: String,
    val displayName: String,
    val role: String, // "PATIENT" o "PROFESSIONAL"
    val token: String
)
