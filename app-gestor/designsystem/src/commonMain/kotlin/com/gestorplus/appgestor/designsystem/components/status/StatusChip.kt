package com.gestorplus.appgestor.designsystem.components.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.AppTheme

@Composable
fun StatusChip(
    text: String,
    backgroundColor: Color,
    contentColor: Color = Color.White
) {
    Box(
        modifier = Modifier
            .background(
                color = backgroundColor.copy(alpha = 0.2f),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            color = backgroundColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
