package com.gestorplus.appgestor.auth.presentation.landing

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.gestorplus.appgestor.auth.presentation.landing.screen.LandingScreen
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import org.junit.Rule
import org.junit.Test

class LandingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun landingScreen_displaysAllOptions() {
        composeTestRule.setContent {
            DsTheme {
                LandingScreen(
                    onNavigateToPatient = {},
                    onNavigateToProfessional = {},
                    onNavigateToOnboarding = {}
                )
            }
        }

        // Verifica que el título principal y las opciones estén presentes
        composeTestRule.onNodeWithText("SoloBook").assertIsDisplayed()
        composeTestRule.onNodeWithText("Soy Paciente").assertIsDisplayed()
        composeTestRule.onNodeWithText("Soy Profesional").assertIsDisplayed()
    }

    @Test
    fun landingScreen_onboardingClick_triggersCallback() {
        var onboardingClicked = false
        
        composeTestRule.setContent {
            DsTheme {
                LandingScreen(
                    onNavigateToPatient = {},
                    onNavigateToProfessional = {},
                    onNavigateToOnboarding = { onboardingClicked = true }
                )
            }
        }

        // Simula clic en el botón de volver al onboarding
        composeTestRule.onNodeWithText("Volver a ver onboarding").performClick()

        assert(onboardingClicked)
    }
}
