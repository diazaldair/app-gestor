package com.gestorplus.appgestor.profile.presentation.state

data class ProfileState(
    val name: String = "",
    val imageUrl: String = "",
    val email: String = "",
    val phone: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val isEditing: Boolean = false
)
