package com.gestorplus.appgestor.presentation.booking.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DarkBackground = Color(0xFF0F172A)
val SurfaceColor = Color(0xFF1E293B)
val PrimaryBlue = Color(0xFF3B82F6)
val SelectedBlue = Color(0xFF2563EB)
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val AccentBlue = Color(0xFF60A5FA)

private val BookingColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    background = DarkBackground,
    surface = SurfaceColor,
    onPrimary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    secondary = AccentBlue,
    tertiary = SelectedBlue
)

@Composable
fun BookingTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BookingColorScheme,
        content = content
    )
}
