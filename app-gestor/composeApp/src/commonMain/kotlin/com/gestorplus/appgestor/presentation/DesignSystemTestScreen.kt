package com.gestorplus.appgestor.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gestorplus.appgestor.designsystem.components.button.PrimaryButton
import com.gestorplus.appgestor.designsystem.components.divider.HorizontalDivider
import com.gestorplus.appgestor.designsystem.components.input.BasicInput
import com.gestorplus.appgestor.designsystem.theme.AppTheme

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
            text = "Prueba de Design System",
            color = AppTheme.colors.primary,
            style = AppTheme.typography.headlineLarge
        )

        HorizontalDivider()

        BasicInput(
            modifier = Modifier.fillMaxWidth(),
            value = textValue,
            onValueChange = { textValue = it },
            label = "Escribe algo aquí..."
        )

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = if (isLoading) "Cargando..." else "Enviar Datos",
            isLoading = isLoading,
            onClick = {
                // Simulamos una carga
                isLoading = true
            }
        )

        if (isLoading) {
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Restablecer Botón",
                onClick = { isLoading = false }
            )
        }
        
        Text(
            text = "Valor ingresado: $textValue",
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.textPrimary
        )
    }
}
