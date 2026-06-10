package com.gestorplus.appgestor.auth.presentation.register

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.gestorplus.appgestor.auth.presentation.register.screen.RegisterScreen
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import org.junit.Rule
import org.junit.Test

class RegisterScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun registerScreen_displaysHeaderAndFields() {
        composeTestRule.setContent {
            DsTheme {
                RegisterScreen(
                    onNavigateToHome = {},
                    onNavigateToLogin = {}
                )
            }
        }

        // Verifica elementos de la cabecera
        composeTestRule.onNodeWithText("SoloBook Pro").assertIsDisplayed()
        
        // Verifica que los labels de los campos estén presentes
        composeTestRule.onNodeWithText("Nombre completo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Correo electrónico").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
        composeTestRule.onNodeWithText("Confirmar contraseña").assertIsDisplayed()
        
        // Verifica el botón principal
        composeTestRule.onNodeWithText("Crear Cuenta").assertIsDisplayed()
    }

    @Test
    fun registerScreen_navigateToLogin_triggersCallback() {
        var loginNavigated = false
        
        composeTestRule.setContent {
            DsTheme {
                RegisterScreen(
                    onNavigateToHome = {},
                    onNavigateToLogin = { loginNavigated = true }
                )
            }
        }

        // Clic en el enlace de iniciar sesión
        composeTestRule.onNodeWithText("Iniciar sesión").performClick()

        assert(loginNavigated)
    }
}
