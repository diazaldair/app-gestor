package com.gestorplus.appgestor.notifications

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.notifications.presentation.screen.NotificationsScreen
import org.junit.Rule
import org.junit.Test

class NotificationsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun notificationsScreen_displaysHeaderAndBottomNav() {
        composeTestRule.setContent {
            DsTheme {
                NotificationsScreen(
                    onNavigateToHome = {},
                    onNavigateToAppointments = {},
                    onNavigateToProfile = {}
                )
            }
        }

        // Verifica el título y la barra de navegación inferior
        composeTestRule.onNodeWithText("Notificaciones").assertIsDisplayed()
        composeTestRule.onNodeWithText("AVISOS").assertIsDisplayed()
    }
}
