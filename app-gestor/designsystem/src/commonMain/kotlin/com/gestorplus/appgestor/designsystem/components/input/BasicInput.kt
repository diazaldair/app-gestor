package com.gestorplus.appgestor.designsystem.components.input

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gestorplus.appgestor.designsystem.theme.AppTheme

@Composable
fun BasicInput(
   value: String,
   onValueChange: (String) -> Unit,
   label: String,
   modifier: Modifier = Modifier,
   enabled: Boolean = true,
   singleLine: Boolean = true,
   minLines: Int = 1
) {
   OutlinedTextField(
       value = value,
       onValueChange = onValueChange,
       modifier = modifier,
       enabled = enabled,
       singleLine = singleLine,
       minLines = minLines,
       label = { if (label.isNotBlank()) Text(label) },
       textStyle = AppTheme.typography.bodyMedium.copy(
           color = Color.White
       ),
       colors = OutlinedTextFieldDefaults.colors(
           focusedTextColor = Color.White,
           unfocusedTextColor = Color.White,
           disabledTextColor = Color.White.copy(alpha = 0.38f),
           focusedBorderColor = AppTheme.colors.primary,
           unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
           disabledBorderColor = Color.White.copy(alpha = 0.12f),
           focusedLabelColor = AppTheme.colors.primary,
           unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
           cursorColor = Color.White,
           selectionColors = TextSelectionColors(
               handleColor = AppTheme.colors.primary,
               backgroundColor = AppTheme.colors.primary.copy(alpha = 0.4f)
           ),
           focusedContainerColor = Color.Transparent,
           unfocusedContainerColor = Color.Transparent,
           disabledContainerColor = Color.Transparent
       ),
       shape = RoundedCornerShape(12.dp)
   )
}
