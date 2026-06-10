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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

private val DarkBackground = Color(0xFF0F172A)
private val CardBackground = Color(0xFF1E293B)
private val PrimaryBlue = Color(0xFF3B82F6)

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
                            "Confirmación",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.onBackClicked() }) {
                            Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBackground)
                )
            },
            containerColor = DarkBackground,
            bottomBar = {
                // Puedes agregar un bottom bar si lo deseas
            }
        ) { paddingValues ->
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryBlue)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress Bars
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        repeat(4) { index ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(if (index <= 2) PrimaryBlue else Color.White.copy(alpha = 0.2f))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Service Card
                    ServiceDetailCard(
                        clinicName = state.clinic?.name ?: "Clínica",
                        serviceName = state.service?.name ?: "Servicio",
                        date = "Oct ${state.date}, 2023",
                        time = state.timeSlot,
                        imageUrl = state.clinic?.imageUrl
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Notes Section
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notes, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Notas adicionales o peticiones", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.notes,
                        onValueChange = { viewModel.onNotesChanged(it) },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        placeholder = { Text("Ej. Indique si tiene alguna alergia médica...", color = Color.Gray, fontSize = 14.sp) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue.copy(alpha = 0.5f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                            focusedContainerColor = CardBackground.copy(alpha = 0.3f),
                            unfocusedContainerColor = CardBackground.copy(alpha = 0.3f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Price Breakdown
                    PriceSection(price = state.service?.price ?: 0.0)

                    Spacer(modifier = Modifier.height(32.dp))

                    // Buttons
                    Button(
                        onClick = { viewModel.onConfirmClicked() },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Confirmar Reserva", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.CheckCircle, null, tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        onClick = { viewModel.onBackClicked() },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.05f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("Volver y editar", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun ServiceDetailCard(
    clinicName: String,
    serviceName: String,
    date: String,
    time: String,
    imageUrl: String?
) {
    Surface(
        color = CardBackground.copy(alpha = 0.5f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                AsyncImage(
                    model = imageUrl ?: "https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?q=80&w=2053&auto=format&fit=crop",
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))))
                )
                
                Column(modifier = Modifier.padding(16.dp).align(Alignment.BottomStart)) {
                    Surface(
                        color = PrimaryBlue.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.4f))
                    ) {
                        Text(
                            "PREMIUM SERVICE", 
                            color = PrimaryBlue, 
                            fontSize = 10.sp, 
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        serviceName, 
                        color = Color.White, 
                        fontSize = 18.sp, 
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(48.dp).background(Color.Gray.copy(alpha = 0.2f), CircleShape))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("PROFESIONAL", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(clinicName, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Row(modifier = Modifier.fillMaxWidth()) {
                    DetailItem(Icons.Default.CalendarToday, date, modifier = Modifier.weight(1f))
                    DetailItem(Icons.Default.AccessTime, time, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun DetailItem(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text("DETALLE", color = Color.Gray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            Text(text, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PriceSection(price: Double) {
    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Costo Total", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                Text("$${price}", color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}
