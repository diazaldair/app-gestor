package com.gestorplus.appgestor.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.home.presentation.screen.HomeScreen
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_displaysWelcomeContent() {
        composeTestRule.setContent {
            DsTheme {
                HomeScreen(
                    onNavigateToProfiles = {},
                    onNavigateToOnboarding = {}
                )
            }
        }

        // Verifica elementos clave de la Home
        composeTestRule.onNodeWithText("GestorPlus").assertIsDisplayed()
        composeTestRule.onNodeWithText("Transforma tu manera de trabajar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Continuar").assertIsDisplayed()
    }

    @Test
    fun homeScreen_navigateToProfiles_triggersCallback() {
        var profilesNavigated = false
        
        composeTestRule.setContent {
            DsTheme {
                HomeScreen(
                    onNavigateToProfiles = { profilesNavigated = true },
                    onNavigateToOnboarding = {}
                )
            }
        }

        // Clic en el botón principal
        composeTestRule.onNodeWithText("Continuar").performClick()

        assert(profilesNavigated)
    }
}
