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
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDashboardScreen(
    viewModel: OwnerDashboardViewModel = koinViewModel()
) {
    val bookings by viewModel.bookings.collectAsState()

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
                                else -> Color.Gray
                            },
                            onDetailsClick = { /* Ver detalles */ },
                            onMessageClick = { /* Enviar mensaje */ }
                        )
                    }
                }
            }
        }
    }
}
