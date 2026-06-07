package com.gestorplus.appgestor.booking.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

private val DarkBackground = Color(0xFF0D1117)
private val CardBackground = Color(0xFF161B22)
private val AccentBlue = Color(0xFF2F81F7)
private val TextGray = Color(0xFF8B949E)
private val StatusOrange = Color(0xFFF97316)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookingsScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MedicalServices, contentDescription = null, tint = AccentBlue)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Mis Reservas", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground,
        bottomBar = {
            // Reusing the same bottom nav pattern
            BottomNavigationBar()
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
            
            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardBackground)
                    .padding(4.dp)
            ) {
                TabItem("Próximas", true, Modifier.weight(1f))
                TabItem("Historial", false, Modifier.weight(1f))
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Recent Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Reciente", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                
                Surface(
                    color = StatusOrange.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "PENDIENTE DE CONFIRMACIÓN",
                        color = StatusOrange,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Booking Card
            BookingCard(
                doctorName = "Dra. Elena Martínez",
                service = "Consulta General",
                date = "24 Oct",
                time = "10:00 AM"
            )
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun TabItem(text: String, isSelected: Boolean, modifier: Modifier = Modifier) {
    Surface(
        color = if (isSelected) Color(0xFF21262D) else Color.Transparent,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Box(modifier = Modifier.padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = if (isSelected) Color.White else TextGray,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun BookingCard(
    doctorName: String,
    service: String,
    date: String,
    time: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Gray.copy(alpha = 0.2f))
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(doctorName, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(service, color = TextGray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarToday, null, tint = AccentBlue, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(date, color = Color.White, fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(Icons.Default.AccessTime, null, tint = AccentBlue, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(time, color = Color.White, fontSize = 12.sp)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Timeline
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimelineStep("Solicitud", true, true)
                TimelineDivider(true)
                TimelineStep("En Revisión", true, false)
                TimelineDivider(false)
                TimelineStep("Confirmada", false, false)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = {}) {
                    Text("Ver detalles", color = AccentBlue, fontWeight = FontWeight.Bold)
                }
                TextButton(onClick = {}) {
                    Text("Cancelar", color = Color(0xFFF85149), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun TimelineStep(label: String, isCompleted: Boolean, isChecked: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isCompleted) AccentBlue else Color.Transparent)
                .border(2.dp, if (isCompleted) AccentBlue else TextGray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isChecked) {
                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
            } else if (isCompleted) {
                Icon(Icons.Default.Refresh, null, tint = Color.White, modifier = Modifier.size(14.dp))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = if (isCompleted) AccentBlue else TextGray, fontSize = 10.sp)
    }
}

@Composable
private fun TimelineDivider(isActive: Boolean) {
    Box(
        modifier = Modifier
            .height(2.dp)
            .width(40.dp)
            .background(if (isActive) AccentBlue else TextGray)
    )
}

@Composable
private fun BottomNavigationBar() {
    Surface(
        color = DarkBackground,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(Icons.Default.Search, "Explorar", false)
            NavItem(Icons.Default.CalendarToday, "Mis Citas", true)
            NavItem(Icons.Default.Person, "Perfil", false)
        }
    }
}

@Composable
private fun NavItem(icon: ImageVector, label: String, isSelected: Boolean) {
    val color = if (isSelected) AccentBlue else TextGray
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = color, fontSize = 10.sp)
    }
}
