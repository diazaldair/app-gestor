package com.gestorplus.appgestor.booking

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.gestorplus.appgestor.booking.presentation.screen.BookingScreen
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import org.junit.Rule
import org.junit.Test

class BookingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bookingScreen_displaysCalendarAndSections() {
        composeTestRule.setContent {
            DsTheme {
                BookingScreen(
                    clinicId = "test-clinic",
                    serviceId = "test-service",
                    onBack = {},
                    onConfirm = {}
                )
            }
        }

        // Verifica que se muestren las secciones de la pantalla
        composeTestRule.onNodeWithText("Available Time Slots").assertIsDisplayed()
        composeTestRule.onNodeWithText("MORNING").assertIsDisplayed()
        composeTestRule.onNodeWithText("AFTERNOON").assertIsDisplayed()
    }
}
