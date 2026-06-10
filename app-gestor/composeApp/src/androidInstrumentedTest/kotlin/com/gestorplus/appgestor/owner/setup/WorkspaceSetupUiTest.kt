package com.gestorplus.appgestor.owner.setup

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.owner.setup_intro.presentation.screen.WorkspaceSetupIntroScreen
import com.gestorplus.appgestor.owner.setup_profile.presentation.screen.WorkspaceSetupProfileScreen
import org.junit.Rule
import org.junit.Test

class WorkspaceSetupUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun setupIntro_displaysCorrectContent() {
        composeTestRule.setContent {
            DsTheme {
                WorkspaceSetupIntroScreen(onNavigateToNextStep = {})
            }
        }

        composeTestRule.onNodeWithText("WORKSPACE READY").assertIsDisplayed()
        composeTestRule.onNodeWithText("Crear mi Consultorio").assertIsDisplayed()
    }

    @Test
    fun setupProfile_displaysFormFields() {
        composeTestRule.setContent {
            DsTheme {
                WorkspaceSetupProfileScreen(
                    onNavigateToNextStep = {},
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Perfil Profesional").assertIsDisplayed()
        composeTestRule.onNodeWithText("NOMBRE DE LA CLÍNICA / CONSULTORIO").assertIsDisplayed()
        composeTestRule.onNodeWithText("BIOGRAFÍA").assertIsDisplayed()
        composeTestRule.onNodeWithText("AÑADIR FOTO").assertIsDisplayed()
        composeTestRule.onNodeWithText("Continuar a Servicios").assertIsDisplayed()
    }
}
