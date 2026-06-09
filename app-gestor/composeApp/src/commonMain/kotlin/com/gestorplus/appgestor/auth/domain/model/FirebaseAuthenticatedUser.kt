package com.gestorplus.appgestor.auth.domain.model

data class FirebaseAuthenticatedUser(
    val uid: String,
    val email: String,
    val displayName: String,
    val isNewUser: Boolean
)
