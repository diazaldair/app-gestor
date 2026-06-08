package com.gestorplus.appgestor.auth.domain.model

enum class UserRole(val firebasePath: String) {
    PATIENT("patients"),
    PROFESSIONAL("doctors")
}
