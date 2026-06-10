package com.gestorplus.appgestor.services

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.services.presentation.screen.ServicesCatalogScreen
import org.junit.Rule
import org.junit.Test

class ServicesCatalogUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun servicesCatalog_displaysHeaderAndSearch() {
        composeTestRule.setContent {
            DsTheme {
                ServicesCatalogScreen(
                    onBack = {},
                    onNavigateToEdit = {},
                    onOpenMenu = {}
                )
            }
        }

        // Verifica el título y la sección de catálogo
        composeTestRule.onNodeWithText("Servicios").assertIsDisplayed()
        composeTestRule.onNodeWithText("CATÁLOGO MÉDICO").assertIsDisplayed()
        composeTestRule.onNodeWithText("Buscar servicio...").assertIsDisplayed()
    }
}
