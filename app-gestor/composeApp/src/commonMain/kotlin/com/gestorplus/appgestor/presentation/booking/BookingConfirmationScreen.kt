package com.gestorplus.appgestor.presentation.booking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.DsTheme

private val DarkBackground = Color(0xFF0F172A)
private val SurfaceColor = Color(0xFF1E293B)
private val PrimaryBlue = Color(0xFF3B82F6)
private val LightBlue = Color(0xFF93C5FD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingConfirmationScreen(
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {
    var notes by remember { mutableStateOf("") }
    
    DsTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Confirmación", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBackground)
                )
            },
            containerColor = DarkBackground,
            bottomBar = {
                ConfirmationBottomBar()
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                
                // Progress Bars
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .background(if (index <= 1) LightBlue else Color.White.copy(alpha = 0.2f), RoundedCornerShape(2.dp))
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Service Card
                ServiceDetailCard()
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Notes Section
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Notes, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Notas adicionales o peticiones", color = Color.White, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    placeholder = { Text("Ej. Indique si tiene alguna alergia médica o síntomas específicos...", color = Color.Gray, fontSize = 14.sp) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue.copy(alpha = 0.5f),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                        focusedContainerColor = SurfaceColor.copy(alpha = 0.3f),
                        unfocusedContainerColor = SurfaceColor.copy(alpha = 0.3f)
                    )
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Price Breakdown
                PriceBreakdownSection()
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Buttons
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Confirmar Reserva", color = Color(0xFF1E3A8A), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.CheckCircleOutline, null, tint = Color(0xFF1E3A8A))
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Surface(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.05f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Volver y editar", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ServiceDetailCard() {
    Surface(
        color = SurfaceColor.copy(alpha = 0.5f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                // Placeholder for background image
                Box(modifier = Modifier.fillMaxSize().background(
                    Brush.verticalGradient(listOf(Color.DarkGray, Color.Black))
                ))
                
                Column(modifier = Modifier.padding(16.dp).align(Alignment.BottomStart)) {
                    Surface(
                        color = PrimaryBlue.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.4f))
                    ) {
                        Text(
                            "PREMIUM SERVICE", 
                            color = LightBlue, 
                            fontSize = 10.sp, 
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Consulta de Cardiología General", 
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
                        Text("CARDIÓLOGO ESPECIALISTA", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("Dr. Alejandro Mendoza", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Row(modifier = Modifier.fillMaxWidth()) {
                    DetailItem(Icons.Default.CalendarToday, "Oct 5, 2023", modifier = Modifier.weight(1f))
                    DetailItem(Icons.Default.AccessTime, "10:00 AM", modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun DetailItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Color.White, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text("CARDIÓLOGO ESPECIALISTA", color = Color.Gray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            Text(text, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PriceBreakdownSection() {
    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            PriceRow("Consulta Médica", "$80.00")
            Spacer(modifier = Modifier.height(12.dp))
            PriceRow("Electrocardiograma (ECG)", "$45.00")
        }
    }
}

@Composable
fun PriceRow(label: String, price: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
        Text(price, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun ConfirmationBottomBar() {
    Surface(color = DarkBackground, border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.05f))) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 24.dp), horizontalArrangement = Arrangement.SpaceAround) {
            ConfirmationNavItem(Icons.Default.Home, "Inicio", false)
            ConfirmationNavItem(Icons.Default.CalendarToday, "Citas", true)
            ConfirmationNavItem(Icons.Default.Person, "Perfil", false)
            ConfirmationNavItem(Icons.Default.Settings, "Ajustes", false)
        }
    }
}

@Composable
fun ConfirmationNavItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean) {
    val color = if (isSelected) LightBlue else Color.Gray
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, label, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = color, fontSize = 10.sp)
    }
}
