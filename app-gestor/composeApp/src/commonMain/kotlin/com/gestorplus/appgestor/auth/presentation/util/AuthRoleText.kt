package com.gestorplus.appgestor.auth.presentation.util

import com.gestorplus.appgestor.auth.domain.model.UserRole

data class AuthUiTexts(
    val appName: String,
    val subtitle: String,
    val demoButton: String,
    val registerTitle: String,
    val registerSubtitle: String,
    val namePlaceholder: String
)

object AuthRoleTextProvider {
    fun getTexts(role: UserRole): AuthUiTexts {
        return when (role) {
            UserRole.PATIENT -> AuthUiTexts(
                appName = "SoloBook",
                subtitle = "Agenda y gestiona tus citas",
                demoButton = "Entrar como Paciente (Demo)",
                registerTitle = "Crear cuenta de paciente",
                registerSubtitle = "GESTIONA TUS CITAS MÉDICAS",
                namePlaceholder = "Ej. Juan Pérez"
            )
            UserRole.PROFESSIONAL -> AuthUiTexts(
                appName = "SoloBook Pro",
                subtitle = "Gestión profesional de citas",
                demoButton = "Entrar como Doctor (Demo)",
                registerTitle = "Crear cuenta profesional",
                registerSubtitle = "GESTIÓN PROFESIONAL DE CITAS",
                namePlaceholder = "Ej. Dr. Julián Castro"
            )
        }
    }
}
