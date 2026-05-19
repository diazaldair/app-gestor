package com.gestorplus.appgestor.profile.presentation.state

data class ProfileState(
    val imageUrl: String = "",
    val email: String = "",
    val phone: String = "",
    val description: String = "",
    val isLoading: Boolean = false
)
