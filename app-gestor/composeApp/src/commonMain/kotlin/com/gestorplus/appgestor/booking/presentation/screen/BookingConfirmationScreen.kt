package com.gestorplus.appgestor.booking.presentation.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.gestorplus.appgestor.booking.presentation.state.BookingConfirmationEffect
import com.gestorplus.appgestor.booking.presentation.viewmodel.BookingConfirmationViewModel
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingConfirmationScreen(
    onBack: () -> Unit,
    onConfirm: () -> Unit,
    viewModel: BookingConfirmationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                BookingConfirmationEffect.NavigateBack -> onBack()
                BookingConfirmationEffect.NavigateToHome -> onConfirm()
            }
        }
    }

    DsTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Confirmar Cita",
                            style = AppTheme.typography.headlineLarge.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.primary
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.onBackClicked() }) {
                            Icon(Icons.Default.ArrowBack, "Volver", tint = AppTheme.colors.primary)
                        }
                    },
                    actions = {
                        Box(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(1.dp, AppTheme.colors.primary.copy(alpha = 0.2f), CircleShape)
                        ) {
                            AsyncImage(
                                model = state.clinic?.imageUrl ?: "https://lh3.googleusercontent.com/aida-public/AB6AXuB2pPfPfkbOg6u_CwVtbnK-dfIVB53dRvCl8q6KarpG1PvFrxcGNsIS8IlQQX4xaV-TocP_XViW2jmmTOpO5bvoHWG2ukfL-mbObUbfAIAMYHQrdb6pQGgmu7y0hb_e-QxTSb7rTt7txRuKGyMGOdv7ehoum7spGFB_9Q5sOqzxBUPSr83VL_s9TkSXlWPfKDt3rebn_-x5khUMeFXcvTryVG2lxyFVnWXKd7PLdwyLOYk97-FPxtYLpL3jDNGaleyQ8mhuMociNns",
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = AppTheme.colors.background
                    )
                )
            },
            bottomBar = {
                ConfirmBookingFooter(
                    onConfirm = { viewModel.onConfirmClicked() },
                    isLoading = state.isLoading
                )
            },
            containerColor = AppTheme.colors.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                // Progress Steps
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) {
                        Box(
                            modifier = Modifier
                                .width(48.dp)
                                .height(4.dp)
                                .padding(horizontal = 4.dp)
                                .clip(CircleShape)
                                .background(AppTheme.colors.primary)
                        )
                    }
                }

                // Appointment Details
                Text(
                    "DETALLES DE LA CITA",
                    style = AppTheme.typography.labelLarge.copy(
                        fontSize = 11.sp,
                        color = AppTheme.colors.textSecondary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )

                Surface(
                    color = AppTheme.colors.surface.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        DetailRow(
                            icon = Icons.Default.MedicalServices,
                            label = "SERVICIO",
                            value = state.service?.name ?: "Consulta General"
                        )
                        HorizontalDivider(color = AppTheme.colors.primary.copy(alpha = 0.05f), modifier = Modifier.padding(horizontal = 16.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.weight(1f)) {
                                DetailRow(
                                    icon = Icons.Default.CalendarToday,
                                    label = "FECHA",
                                    value = "${state.month.take(3)} ${state.date}"
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                DetailRow(
                                    icon = Icons.Default.Schedule,
                                    label = "HORA",
                                    value = state.timeSlot
                                )
                            }
                        }
                        HorizontalDivider(color = AppTheme.colors.primary.copy(alpha = 0.05f), modifier = Modifier.padding(horizontal = 16.dp))
                        DetailRow(
                            icon = Icons.Default.LocationOn,
                            label = "LUGAR",
                            value = state.clinic?.address ?: "Av. Principal"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Cost Summary
                Text(
                    "INFORMACIÓN DE COSTO",
                    style = AppTheme.typography.labelLarge.copy(
                        fontSize = 11.sp,
                        color = AppTheme.colors.textSecondary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )

                Surface(
                    color = AppTheme.colors.surface.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(AppTheme.colors.primary.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Payments, null, tint = AppTheme.colors.primary, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Costo Total",
                                style = AppTheme.typography.bodyMedium.copy(color = AppTheme.colors.textPrimary)
                            )
                        }
                        Text(
                            "$${state.service?.price ?: "85.00"}",
                            style = AppTheme.typography.headlineLarge.copy(
                                fontSize = 20.sp,
                                color = AppTheme.colors.primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
                
                Text(
                    "El pago se realizará directamente en el local el día de su cita.",
                    style = AppTheme.typography.labelLarge.copy(
                        fontSize = 12.sp,
                        color = AppTheme.colors.textSecondary.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(top = 12.dp, start = 4.dp)
                )

                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}

@Composable
fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AppTheme.colors.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = AppTheme.colors.primary, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                label,
                style = AppTheme.typography.labelLarge.copy(
                    fontSize = 10.sp,
                    color = AppTheme.colors.textSecondary,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                value,
                style = AppTheme.typography.bodyMedium.copy(
                    color = AppTheme.colors.textPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
fun ConfirmBookingFooter(onConfirm: () -> Unit, isLoading: Boolean) {
    Surface(
        color = AppTheme.colors.background.copy(alpha = 0.95f),
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 8.dp,
        border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.1f))
    ) {
        Column {
            Button(
                onClick = onConfirm,
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Confirmar Reserva", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(20.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
