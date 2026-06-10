package com.gestorplus.appgestor.clinic_detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.gestorplus.appgestor.clinic_detail.presentation.ClinicDetailScreen
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import org.junit.Rule
import org.junit.Test

class ClinicDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun clinicDetail_displaysContent() {
        composeTestRule.setContent {
            DsTheme {
                ClinicDetailScreen(
                    clinicId = "test-id",
                    onNavigateBack = {},
                    onNavigateToBooking = { _, _ -> }
                )
            }
        }

        // Verifica elementos de la pantalla
        composeTestRule.onNodeWithText("SoloBook").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bio").assertIsDisplayed()
        composeTestRule.onNodeWithText("Especialidades").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ubicación").assertIsDisplayed()
        composeTestRule.onNodeWithText("Servicios").assertIsDisplayed()
    }
}
