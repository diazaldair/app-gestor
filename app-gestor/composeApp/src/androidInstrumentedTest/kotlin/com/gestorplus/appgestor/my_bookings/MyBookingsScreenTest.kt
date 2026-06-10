package com.gestorplus.appgestor.my_bookings

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.my_bookings.presentation.MyBookingsScreen
import org.junit.Rule
import org.junit.Test

class MyBookingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun myBookings_displaysTabs() {
        composeTestRule.setContent {
            DsTheme {
                MyBookingsScreen(
                    onNavigateToExplore = {},
                    onNavigateToProfile = {}
                )
            }
        }

        // Verifica que se muestren las pestañas de filtro
        composeTestRule.onNodeWithText("Próximas").assertIsDisplayed()
        composeTestRule.onNodeWithText("Historial").assertIsDisplayed()
        
        // Verifica el título
        composeTestRule.onNodeWithText("Mis Reservas").assertIsDisplayed()
    }
}
