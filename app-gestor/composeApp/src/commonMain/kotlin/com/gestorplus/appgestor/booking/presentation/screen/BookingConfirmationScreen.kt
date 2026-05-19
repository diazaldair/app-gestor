package com.gestorplus.appgestor.booking.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.booking.presentation.state.*
import com.gestorplus.appgestor.booking.presentation.viewmodel.BookingViewModel
import org.koin.compose.viewmodel.koinViewModel

private val DarkBackground = Color(0xFF0D1117)
private val CardBackground = Color(0xFF161B22)
private val AccentBlue = Color(0xFF2F81F7)

@Composable
fun BookingConfirmationScreen(
    onBack: () -> Unit,
    onConfirm: () -> Unit,
    viewModel: BookingViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is BookingEfffect.NavigateBack -> onBack()
                is BookingEfffect.BookingConfirmed -> onConfirm()
                else -> {}
            }
        }
    }

    BookingConfirmationContent(
        state = state,
        onEvent = { viewModel.onEvent(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingConfirmationContent(
    state: BookingUiState,
    onEvent: (BookingEvent) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Confirmar Cita", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(BookingEvent.OnBackClicked) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground,
        bottomBar = {
            Box(modifier = Modifier.padding(20.dp).navigationBarsPadding()) {
                Button(
                    onClick = { onEvent(BookingEvent.OnConfirmBooking) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Confirmar Reserva", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            
            // Progress indicators
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(4) { index ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (index <= 2) AccentBlue else Color.Gray.copy(alpha = 0.3f))
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                "DETALLES DE LA CITA",
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    DetailRow(Icons.Default.MedicalServices, "SERVICIO", "Consulta General")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.Gray.copy(alpha = 0.1f))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        DetailRow(Icons.Default.CalendarToday, "FECHA", "Jueves 24 Oct", Modifier.weight(1f))
                        DetailRow(Icons.Default.AccessTime, "HORA", "10:00 AM", Modifier.weight(1f))
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.Gray.copy(alpha = 0.1f))
                    DetailRow(Icons.Default.LocationOn, "LUGAR", "Av ...")
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                "INFORMACIÓN DE COSTO",
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(AccentBlue.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Payments, contentDescription = null, tint = AccentBlue)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Costo Total", color = Color.White, fontSize = 16.sp)
                    }
                    Text(
                        "$85.00",
                        color = AccentBlue,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                "El pago se realizará directamente en el local el día de su cita.",
                color = Color.Gray,
                fontSize = 13.sp,
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun DetailRow(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.05f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}
