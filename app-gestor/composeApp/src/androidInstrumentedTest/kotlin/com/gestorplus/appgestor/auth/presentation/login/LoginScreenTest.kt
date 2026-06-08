package com.gestorplus.appgestor.auth.presentation.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.gestorplus.appgestor.auth.presentation.screen.LoginScreen
import com.gestorplus.appgestor.auth.presentation.state.LoginEvent
import com.gestorplus.appgestor.auth.presentation.state.LoginUiState
import org.junit.Rule
import org.junit.Test

/**
 * Prueba de UI para la pantalla de Login.
 * Verifica que la UI reaccione correctamente al estado y que los inputs
 * disparen los eventos MVI correspondientes.
 */
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_interaction_triggersCorrectEvents() {
        var capturedEvent: LoginEvent? = null

        // Seteamos el contenido de la prueba usando el Composable de la pantalla
        // Inyectamos un estado inicial vacío y capturamos los eventos
        composeTestRule.setContent {
            LoginScreen(
                state = LoginUiState(),
                onEvent = { event -> capturedEvent = event }
            )
        }

        // 1. Verificar escritura en el campo de correo
        composeTestRule.onNodeWithText("Correo electrónico").performTextInput("doctor@clinic.com")
        assert(capturedEvent is LoginEvent.EmailChanged)

        // 2. Verificar escritura en el campo de contraseña
        composeTestRule.onNodeWithText("Contraseña").performTextInput("password123")
        assert(capturedEvent is LoginEvent.PasswordChanged)

        // 3. Verificar que el clic en el botón dispare el evento de login
        composeTestRule.onNodeWithText("Iniciar sesión").performClick()
        assert(capturedEvent is LoginEvent.OnLoginClicked)
    }

    @Test
    fun loginScreen_whenStateHasError_showsErrorMessage() {
        val errorText = "Credenciales incorrectas"

        composeTestRule.setContent {
            LoginScreen(
                state = LoginUiState(errorMessage = errorText),
                onEvent = {}
            )
        }

        // Validar que el mensaje de error definido en el estado sea visible
        composeTestRule.onNodeWithText(errorText).assertIsDisplayed()
    }
}