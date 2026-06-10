package com.gestorplus.appgestor.services_entry

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.services_entry.presentation.screen.ServicesEntryScreen
import com.gestorplus.appgestor.services_entry.presentation.viewmodel.ServicesEntryViewModel
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class ServicesEntryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun servicesEntry_displaysCardsAndStats() {
        val mockViewModel = mock(ServicesEntryViewModel::class.java)
        
        composeTestRule.setContent {
            DsTheme {
                ServicesEntryScreen(
                    viewModel = mockViewModel,
                    onBack = {},
                    onNavigateToCatalog = {},
                    onNavigateToTurns = {},
                    onNavigateToProfile = {},
                    onNavigateToNotifications = {}
                )
            }
        }

        // Verifica elementos clave
        composeTestRule.onNodeWithText("SoloBook Pro").assertIsDisplayed()
        composeTestRule.onNodeWithText("Catálogo de Servicios").assertIsDisplayed()
        composeTestRule.onNodeWithText("Configuración de Turnos").assertIsDisplayed()
        composeTestRule.onNodeWithText("PRÓXIMA CITA").assertIsDisplayed()
    }
}
