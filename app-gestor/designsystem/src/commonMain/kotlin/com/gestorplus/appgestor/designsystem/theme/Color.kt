package com.gestorplus.appgestor.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColors(
   val primary: Color,
   val onPrimary: Color,
   val background: Color,
   val surface: Color,
   val onSurface: Color,
   val textPrimary: Color,
   val textSecondary: Color,
   val accent: Color,
   val error: Color,
   val success: Color,
   val isLight: Boolean
)

// SoloBook Palette - Slate/Dark Industrial Theme (Image match)
val DarkPalette = AppColors(
   primary = Color(0xFF3B82F6), // SoloBook Blue
   onPrimary = Color.White,
   background = Color(0xFF0F172A), // Slate 900
   surface = Color(0xFF1E293B), // Slate 800
   onSurface = Color.White,
   textPrimary = Color.White,
   textSecondary = Color(0xFF94A3B8), // Slate 400
   accent = Color(0xFF60A5FA), // Slate 300
   error = Color(0xFFEF4444),
   success = Color(0xFF10B981),
   isLight = false
)

val LightPalette = AppColors(
   primary = Color(0xFF2563EB),
   onPrimary = Color.White,
   background = Color(0xFFF8FAFC),
   surface = Color.White,
   onSurface = Color(0xFF1E293B),
   textPrimary = Color(0xFF0F172A),
   textSecondary = Color(0xFF64748B),
   accent = Color(0xFF3B82F6),
   error = Color(0xFFDC2626),
   success = Color(0xFF059669),
   isLight = true
)

val HighContrastPalette = AppColors(
   primary = Color(0xFFFFFF00),
   onPrimary = Color.Black,
   background = Color.Black,
   surface = Color.Black,
   onSurface = Color.White,
   textPrimary = Color.White,
   textSecondary = Color.LightGray,
   accent = Color.Cyan,
   error = Color.Red,
   success = Color.Green,
   isLight = false
)
