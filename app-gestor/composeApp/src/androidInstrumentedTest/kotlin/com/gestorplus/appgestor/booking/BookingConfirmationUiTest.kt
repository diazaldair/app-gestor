package com.gestorplus.appgestor.booking

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.gestorplus.appgestor.booking.presentation.screen.BookingConfirmationScreen
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import org.junit.Rule
import org.junit.Test

class BookingConfirmationUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bookingConfirmation_displaysHeaderAndButtons() {
        composeTestRule.setContent {
            DsTheme {
                BookingConfirmationScreen(
                    onBack = {},
                    onConfirm = {}
                )
            }
        }

        // Verifica que aparezca el título de la pantalla
        composeTestRule.onNodeWithText("Confirmación").assertIsDisplayed()
        
        // Verifica que el botón de confirmar esté presente
        composeTestRule.onNodeWithText("Confirmar Reserva").assertIsDisplayed()
    }

    @Test
    fun bookingConfirmation_clickBack_triggersCallback() {
        var backClicked = false
        
        composeTestRule.setContent {
            DsTheme {
                BookingConfirmationScreen(
                    onBack = { backClicked = true },
                    onConfirm = {}
                )
            }
        }

        // Clic en el botón de volver (el de abajo en la Surface)
        composeTestRule.onNodeWithText("Volver y editar").performClick()

        assert(backClicked)
    }
}
