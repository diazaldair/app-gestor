package com.gestorplus.appgestor.booking.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DarkBackground = Color(0xFF0D1117)
private val CardBackground = Color(0xFF161B22)
private val AccentBlue = Color(0xFF2F81F7)
private val OrangeStatus = Color(0xFFE3B341)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyReservationsScreen(
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Próximas", "Historial")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Reservas", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.NotificationsNone, contentDescription = "Notifications", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Custom Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .padding(4.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedTab == index) Color.White.copy(alpha = 0.1f) else Color.Transparent)
                            .clickable { selectedTab = index },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            color = if (selectedTab == index) Color.White else Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Reciente", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    "PENDIENTE DE CONFIRMACIÓN",
                    color = OrangeStatus,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ReservationCard()
        }
    }
}

@Composable
fun ReservationCard() {
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
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Dra. Elena Martínez", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Consulta General", color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("24 Oct", color = Color.White, fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("10:00 AM", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Stepper
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StepItem("Solicitud", Icons.Default.Check, true, true)
                HorizontalDivider(modifier = Modifier.weight(1f).padding(horizontal = 4.dp), color = AccentBlue)
                StepItem("En Revisión", Icons.Default.Sync, true, false)
                HorizontalDivider(modifier = Modifier.weight(1f).padding(horizontal = 4.dp), color = Color.Gray.copy(alpha = 0.3f))
                StepItem("Confirmada", Icons.Default.CalendarToday, false, false)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { }) {
                    Text("Ver detalles", color = AccentBlue)
                }
                TextButton(onClick = { }) {
                    Text("Cancelar", color = Color(0xFFF85149))
                }
            }
        }
    }
}

@Composable
fun StepItem(label: String, icon: ImageVector, isActive: Boolean, isCompleted: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isActive) AccentBlue.copy(alpha = 0.2f) else Color.Transparent)
                .border(1.dp, if (isActive) AccentBlue else Color.Gray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isActive) AccentBlue else Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = if (isActive) Color.White else Color.Gray, fontSize = 10.sp)
    }
}
