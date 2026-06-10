package com.gestorplus.appgestor.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

enum class ThemeMode {
   LIGHT,
   DARK,
   HIGH_CONTRAST
}

val LocalColors = staticCompositionLocalOf { DarkPalette }
internal val LocalTypography = staticCompositionLocalOf { DefaultTypography }

object AppTheme {
   val colors: AppColors
      @Composable
      @ReadOnlyComposable
      get() = LocalColors.current

   val typography: Typography
      @Composable
      @ReadOnlyComposable
      get() = LocalTypography.current
}

@Composable
fun DsTheme(
   mode: ThemeMode? = null,
   content: @Composable () -> Unit
) {
   // Force ThemeMode.DARK as default to align with Figma designs and prevent the white screen issue.
   val themeMode = mode ?: ThemeMode.DARK

   val colors = when (themeMode) {
      ThemeMode.LIGHT -> LightPalette
      ThemeMode.DARK -> DarkPalette
      ThemeMode.HIGH_CONTRAST -> HighContrastPalette
   }
  
   CompositionLocalProvider(
       LocalColors provides colors,
       LocalTypography provides DefaultTypography
   ) {
       content()
   }
}
