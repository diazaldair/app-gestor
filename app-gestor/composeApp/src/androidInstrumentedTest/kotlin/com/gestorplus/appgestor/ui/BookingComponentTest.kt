package com.gestorplus.appgestor.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.gestorplus.appgestor.booking.presentation.screen.BookingBottomBar
import org.junit.Rule
import org.junit.Test

class BookingComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bookingBottomBar_displaysSelectedDateTime() {
        // Arrange
        val testDate = "Oct 27"
        val testTime = "10:00 AM"

        // Act
        composeTestRule.setContent {
            BookingBottomBar(
                selectedDate = testDate,
                selectedTime = testTime,
                onConfirm = {}
            )
        }

        // Assert
        // Verificamos que aparezca la fecha y hora seleccionada
        composeTestRule.onNodeWithText("Oct 27, 10:00 AM").assertIsDisplayed()
        // Verificamos que el botón de confirmación esté presente
        composeTestRule.onNodeWithText("Confirm Booking").assertIsDisplayed()
    }

    @Test
    fun bookingBottomBar_showsPlaceholder_whenNoTimeSelected() {
        // Act
        composeTestRule.setContent {
            BookingBottomBar(
                selectedDate = "Oct 27",
                selectedTime = null,
                onConfirm = {}
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Oct 27, Select a slot").assertIsDisplayed()
    }

    @Test
    fun bookingBottomBar_clickingConfirm_triggersCallback() {
        var clicked = false
        
        // Act
        composeTestRule.setContent {
            BookingBottomBar(
                selectedDate = "Oct 27",
                selectedTime = "10:00 AM",
                onConfirm = { clicked = true }
            )
        }

        // Click en el botón
        composeTestRule.onNodeWithText("Confirm Booking").performClick()

        // Assert
        assert(clicked)
    }
}
