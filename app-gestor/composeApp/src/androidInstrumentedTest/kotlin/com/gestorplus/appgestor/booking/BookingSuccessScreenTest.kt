package com.gestorplus.appgestor.booking

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.gestorplus.appgestor.booking.presentation.screen.BookingSuccessScreen
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import org.junit.Rule
import org.junit.Test

class BookingSuccessScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bookingSuccess_displaysConfirmationMessage() {
        composeTestRule.setContent {
            DsTheme {
                BookingSuccessScreen(
                    clinicName = "SoloBook Health",
                    doctorImageUrl = null,
                    onGoHome = {},
                    onViewCalendar = {}
                )
            }
        }

        // Verifica mensajes de éxito
        composeTestRule.onNodeWithText("¡Cita Enviada!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ver en mi Calendario").assertIsDisplayed()
        composeTestRule.onNodeWithText("Volver al Inicio").assertIsDisplayed()
    }

    @Test
    fun bookingSuccess_goHome_triggersCallback() {
        var homeNavigated = false
        
        composeTestRule.setContent {
            DsTheme {
                BookingSuccessScreen(
                    clinicName = "SoloBook Health",
                    doctorImageUrl = null,
                    onGoHome = { homeNavigated = true },
                    onViewCalendar = {}
                )
            }
        }

        // Clic en volver al inicio
        composeTestRule.onNodeWithText("Volver al Inicio").performClick()

        assert(homeNavigated)
    }
}
