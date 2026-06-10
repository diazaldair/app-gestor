package com.gestorplus.appgestor.onboarding

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.onboarding.presentation.screen.OnboardingScreen
import org.junit.Rule
import org.junit.Test

class OnboardingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun onboardingScreen_canSkipAndNavigate() {
        var navigatedToHome = false

        composeTestRule.setContent {
            DsTheme {
                OnboardingScreen(
                    onNavigateToHome = { navigatedToHome = true }
                )
            }
        }

        // Verifica que el botón "Omitir" esté presente (ajustar según idioma por defecto)
        // En tu código usas localizedText, por defecto suele ser Inglés si no hay estado previo
        // pero en el ViewModel suele inicializarse. 
        // Si no inyectamos Mock ViewModel, usará el real.
        
        try {
            composeTestRule.onNodeWithText("Skip").assertIsDisplayed().performClick()
        } catch (e: AssertionError) {
            // Intentamos con español si falla el inglés
            composeTestRule.onNodeWithText("Omitir").assertIsDisplayed().performClick()
        }

        // Debería disparar la navegación al omitir (depende de la lógica del ViewModel)
        assert(navigatedToHome)
    }
}
