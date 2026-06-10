package com.gestorplus.appgestor.owner.dashboard

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.owner.dashboard.presentation.screen.OwnerDashboardScreen
import org.junit.Rule
import org.junit.Test

class OwnerDashboardScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun ownerDashboard_displaysHeaderAndSections() {
        composeTestRule.setContent {
            DsTheme {
                OwnerDashboardScreen(
                    onBack = {},
                    onNavigateToWorkingHours = {},
                    onNavigateToProfile = {},
                    onNavigateToServices = {}
                )
            }
        }

        // Verifica que se muestre el nombre de la app/título
        composeTestRule.onNodeWithText("SoloBook").assertIsDisplayed()
        
        // Verifica que se muestren las secciones de agenda
        // (Nota: Res.string.owner_agenda_today suele ser "Agenda de Hoy" o similar)
        // Como no puedo resolver los recursos de cadena fácilmente en la prueba sin contexto, 
        // uso textos que sé que están ahí.
        composeTestRule.onNodeWithText("9:00 AM - 6:00 PM").assertIsDisplayed()
    }
}
