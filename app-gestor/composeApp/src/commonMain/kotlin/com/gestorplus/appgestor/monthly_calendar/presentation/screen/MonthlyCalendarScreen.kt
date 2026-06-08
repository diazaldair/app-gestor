package com.gestorplus.appgestor.monthly_calendar.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gestorplus.appgestor.monthly_calendar.presentation.viewmodel.MonthlyCalendarViewModel

@Composable
fun MonthlyCalendarScreen(
    viewModel: MonthlyCalendarViewModel,
    onNavigateToSettings: () -> Unit
) {
    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text("Calendario Mensual (En desarrollo)")
            Button(onClick = onNavigateToSettings) {
                Text("Ir a Configuración de Turnos")
            }
        }
    }
}
