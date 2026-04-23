package com.gestorplus.appgestor.presentation.owner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gestorplus.appgestor.designsystem.components.card.AppointmentCard
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDashboardScreen(
    viewModel: OwnerDashboardViewModel = koinViewModel()
) {
    val bookings by viewModel.bookings.collectAsState()
    val firebaseLogs by viewModel.firebaseLogs.collectAsState()
    val isLogsLoading by viewModel.isLogsLoading.collectAsState()
    var showLogsDialog by remember { mutableStateOf(false) }

    DsTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Agenda de Hoy", style = AppTheme.typography.headlineLarge) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = AppTheme.colors.background
                    )
                )
            },
            containerColor = AppTheme.colors.background
        ) { paddingValues ->
            if (bookings.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues), 
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("No hay citas registradas", color = AppTheme.colors.textPrimary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(bookings) { booking ->
                        AppointmentCard(
                            clientName = booking.clientName,
                            serviceName = booking.serviceName,
                            duration = "${booking.durationMinutes} min",
                            statusText = booking.status,
                            statusColor = when(booking.status) {
                                "CONFIRMED" -> Color(0xFF4CAF50)
                                "PENDING" -> Color(0xFFFF9800)
                                "PAID" -> Color(0xFF2196F3)
                                "FEATURED" -> Color(0xFF00BCD4) // Cian para sincronización
                                else -> Color.Gray
                            },
                            onDetailsClick = { /* Ver detalles */ },
                            onMessageClick = { /* Enviar mensaje */ }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { 
                                viewModel.loadFirebaseLogs()
                                showLogsDialog = true 
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                        ) {
                            Text(" Ver Logs del Ejercicio 4 (Desde Firebase)")
                        }
                    }
                }
            }
        }

        if (showLogsDialog) {
            AlertDialog(
                onDismissRequest = { showLogsDialog = false },
                title = { Text("App Logs (Realtime Database)") },
                text = {
                    Column {
                        if (isLogsLoading) {
                            CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.CenterHorizontally))
                        } else if (firebaseLogs.isEmpty()) {
                            Text("No se encontraron logs en Firebase.")
                        } else {
                            LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                                items(firebaseLogs) { log ->
                                    Text(
                                        text = "• $log",
                                        style = AppTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                    Divider(color = Color.LightGray.copy(alpha = 0.5f))
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showLogsDialog = false }) {
                        Text("Cerrar")
                    }
                }
            )
        }
    }
}
