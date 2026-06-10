package com.gestorplus.appgestor.owner.setup

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.owner.setup_schedule.presentation.screen.WorkspaceSetupScheduleScreen
import com.gestorplus.appgestor.owner.setup_service.presentation.screen.WorkspaceSetupServiceScreen
import com.gestorplus.appgestor.owner.setup_success.presentation.screen.WorkspaceSetupSuccessScreen
import org.junit.Rule
import org.junit.Test

class WorkspaceSetupStepsUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun setupSchedule_displaysContent() {
        composeTestRule.setContent {
            DsTheme {
                WorkspaceSetupScheduleScreen(
                    onNavigateToNextStep = {},
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Horarios y Disponibilidad").assertIsDisplayed()
        composeTestRule.onNodeWithText("Días Laborales").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rangos de Atención").assertIsDisplayed()
        composeTestRule.onNodeWithText("Continuar y Finalizar").assertIsDisplayed()
    }

    @Test
    fun setupService_displaysForm() {
        composeTestRule.setContent {
            DsTheme {
                WorkspaceSetupServiceScreen(
                    onNavigateToNextStep = {},
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Nuevo Servicio").assertIsDisplayed()
        composeTestRule.onNodeWithText("NOMBRE DEL SERVICIO").assertIsDisplayed()
        composeTestRule.onNodeWithText("DESCRIPCIÓN").assertIsDisplayed()
        composeTestRule.onNodeWithText("DURACIÓN DEL SERVICIO").assertIsDisplayed()
        composeTestRule.onNodeWithText("Finalizar").assertIsDisplayed()
    }

    @Test
    fun setupSuccess_displaysActiveProfile() {
        composeTestRule.setContent {
            DsTheme {
                WorkspaceSetupSuccessScreen(
                    onNavigateToDashboard = {}
                )
            }
        }

        composeTestRule.onNodeWithText("¡Perfil Activo!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ir a mi Consola").assertIsDisplayed()
    }
}
