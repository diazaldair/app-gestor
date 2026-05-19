package com.gestorplus.appgestor.auth.data.datasource.dto

data class AuthUserDto(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val role: String = "PROFESSIONAL",
    val token: String = ""
)
