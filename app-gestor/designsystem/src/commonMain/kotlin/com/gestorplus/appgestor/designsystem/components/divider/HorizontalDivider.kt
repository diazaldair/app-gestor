package com.gestorplus.appgestor.designsystem.components.divider

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gestorplus.appgestor.designsystem.theme.AppTheme

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 1.dp,
        color = AppTheme.colors.textPrimary.copy(alpha = 0.1f)
    )
}
