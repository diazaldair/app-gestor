package com.gestorplus.appgestor.core.designsystem.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gestorplus.appgestor.designsystem.components.button.PrimaryButton
import com.gestorplus.appgestor.designsystem.components.divider.HorizontalDivider
import com.gestorplus.appgestor.designsystem.components.input.BasicInput
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import app_gestor.composeapp.generated.resources.*

@Composable
fun DesignSystemTestScreen() {
    var textValue by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.ds_test_title),
            color = AppTheme.colors.primary,
            style = AppTheme.typography.headlineLarge
        )

        HorizontalDivider()

        BasicInput(
            modifier = Modifier.fillMaxWidth(),
            value = textValue,
            onValueChange = { textValue = it },
            label = stringResource(Res.string.ds_test_input_label)
        )

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = if (isLoading) stringResource(Res.string.common_loading) else stringResource(Res.string.common_send),
            isLoading = isLoading,
            onClick = {
                isLoading = true
            }
        )

        if (isLoading) {
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.common_reset),
                onClick = { isLoading = false }
            )
        }
        
        Text(
            text = stringResource(Res.string.ds_test_result_label, textValue),
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.textPrimary
        )
    }
}
