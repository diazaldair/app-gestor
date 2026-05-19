package com.gestorplus.appgestor.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class Typography(
   val headlineLarge: TextStyle,
   val headlineMedium: TextStyle,
   val headlineSmall: TextStyle,
   val bodyLarge: TextStyle,
   val bodyMedium: TextStyle,
   val bodySmall: TextStyle,
   val labelLarge: TextStyle,
   val labelMedium: TextStyle,
   val labelSmall: TextStyle,
)

val DefaultTypography = Typography(
   headlineLarge = TextStyle(
       fontWeight = FontWeight.Bold,
       fontSize = 32.sp,
       lineHeight = 40.sp
   ),
   headlineMedium = TextStyle(
       fontWeight = FontWeight.Bold,
       fontSize = 24.sp,
       lineHeight = 32.sp
   ),
   headlineSmall = TextStyle(
       fontWeight = FontWeight.Bold,
       fontSize = 20.sp,
       lineHeight = 28.sp
   ),
   bodyLarge = TextStyle(
       fontWeight = FontWeight.Normal,
       fontSize = 18.sp,
       lineHeight = 26.sp
   ),
   bodyMedium = TextStyle(
       fontWeight = FontWeight.Normal,
       fontSize = 16.sp,
       lineHeight = 24.sp
   ),
   bodySmall = TextStyle(
       fontWeight = FontWeight.Normal,
       fontSize = 14.sp,
       lineHeight = 20.sp
   ),
   labelLarge = TextStyle(
       fontWeight = FontWeight.Medium,
       fontSize = 14.sp,
       lineHeight = 20.sp,
       letterSpacing = 0.1.sp
   ),
   labelMedium = TextStyle(
       fontWeight = FontWeight.Medium,
       fontSize = 12.sp,
       lineHeight = 16.sp,
       letterSpacing = 0.5.sp
   ),
   labelSmall = TextStyle(
       fontWeight = FontWeight.Medium,
       fontSize = 10.sp,
       lineHeight = 16.sp,
       letterSpacing = 0.1.sp
   )
)
