package com.gestorplus.appgestor.profile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.profile.presentation.screen.ProfileScreen
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun profileScreen_displaysProfileInfo() {
        composeTestRule.setContent {
            DsTheme {
                ProfileScreen(onBack = {})
            }
        }

        // Verifica elementos del perfil
        composeTestRule.onNodeWithText("Mi Perfil Profesional").assertIsDisplayed()
        composeTestRule.onNodeWithText("Información General").assertIsDisplayed()
        composeTestRule.onNodeWithText("Nombre Completo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Correo Electrónico").assertIsDisplayed()
    }
}
