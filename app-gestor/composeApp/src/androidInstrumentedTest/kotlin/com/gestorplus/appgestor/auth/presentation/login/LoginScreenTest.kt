package com.gestorplus.appgestor.auth.presentation.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.gestorplus.appgestor.auth.presentation.login.screen.LoginScreen
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysElements() {
        composeTestRule.setContent {
            DsTheme {
                LoginScreen(
                    onNavigateToHome = {},
                    onNavigateToRegister = {}
                )
            }
        }

        // Verifica que los textos descriptivos aparezcan
        composeTestRule.onNodeWithText("SoloBook Pro").assertIsDisplayed()
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsDisplayed()
    }

    @Test
    fun loginScreen_navigateToRegister_triggersCallback() {
        var registerNavigated = false
        
        composeTestRule.setContent {
            DsTheme {
                LoginScreen(
                    onNavigateToHome = {},
                    onNavigateToRegister = { registerNavigated = true }
                )
            }
        }

        // Clic en el enlace de crear cuenta
        composeTestRule.onNodeWithText("Crear cuenta").performClick()

        assert(registerNavigated)
    }
}
