package com.gestorplus.appgestor.explore_clinics

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.explore_clinics.presentation.ExploreClinicsScreen
import org.junit.Rule
import org.junit.Test

class ExploreClinicsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun exploreClinics_displaysTopBarAndBottomNav() {
        composeTestRule.setContent {
            DsTheme {
                ExploreClinicsScreen(
                    onNavigateToClinicDetail = {},
                    onNavigateToMyAppointments = {},
                    onNavigateToProfile = {}
                )
            }
        }

        // Verifica el título en el TopAppBar
        composeTestRule.onNodeWithText("SoloBook Health").assertIsDisplayed()
        
        // Verifica elementos del Bottom Nav
        composeTestRule.onNodeWithText("Explorar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mis Citas").assertIsDisplayed()
        composeTestRule.onNodeWithText("Perfil").assertIsDisplayed()
    }

    @Test
    fun exploreClinics_navigateToAppointments_triggersCallback() {
        var appointmentsNavigated = false
        
        composeTestRule.setContent {
            DsTheme {
                ExploreClinicsScreen(
                    onNavigateToClinicDetail = {},
                    onNavigateToMyAppointments = { appointmentsNavigated = true },
                    onNavigateToProfile = {}
                )
            }
        }

        // Clic en el botón de la barra inferior
        composeTestRule.onNodeWithText("Mis Citas").performClick()

        assert(appointmentsNavigated)
    }
}
