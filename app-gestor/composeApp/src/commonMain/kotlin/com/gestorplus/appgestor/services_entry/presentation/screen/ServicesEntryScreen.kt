package com.gestorplus.appgestor.services_entry.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.services_entry.presentation.state.ServicesEntryUiState
import com.gestorplus.appgestor.services_entry.presentation.viewmodel.ServicesEntryViewModel
import org.jetbrains.compose.resources.painterResource
import appprogramovil.composeapp.generated.resources.Res
import appprogramovil.composeapp.generated.resources.compose_multiplatform

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesEntryScreen(
    viewModel: ServicesEntryViewModel,
    onOpenMenu: () -> Unit,
    onNavigateToCatalog: () -> Unit,
    onNavigateToTurns: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color(0xFF0F172A),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(Res.drawable.compose_multiplatform),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp).clip(CircleShape)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text("SoloBook Pro", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onOpenMenu) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.NotificationsNone, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text("Buenos días,", color = Color.Gray, fontSize = 16.sp)
            Text(state.professionalName, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard("CITAS", state.appointmentsCount.toString(), Modifier.weight(1f))
                StatCard("PENDIENTES", "${state.pendingCount} >", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            MenuOptionCard(
                icon = Icons.Default.MedicalServices,
                title = "Catálogo de Servicios",
                subtitle = "Gestiona tus consultas y procedimientos",
                onClick = onNavigateToCatalog
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            MenuOptionCard(
                icon = Icons.Default.Schedule,
                title = "Configuración de Turnos",
                subtitle = "Gestiona tus horarios y disponibilidad",
                onClick = onNavigateToTurns
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text("PRÓXIMA CITA", color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            NextAppointmentCard()
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("RESTO DEL DÍA", color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("Ver Todo", color = Color(0xFF3B82F6), fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(value, color = Color(0xFF3B82F6), fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MenuOptionCard(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(Color(0xFF334155), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = Color(0xFFF59E0B))
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold)
                Text(subtitle, color = Color.Gray, fontSize = 12.sp)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
        }
    }
}

@Composable
fun NextAppointmentCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).background(Color(0xFF334155), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, null, tint = Color(0xFF3B82F6))
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Mariana Flores", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Check-up General", color = Color.Gray, fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("09:30 AM", color = Color(0xFF3B82F6), fontWeight = FontWeight.Bold)
                    Text("EN 15 MIN", color = Color.Gray, fontSize = 10.sp)
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Iniciar Consulta", fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(12.dp))
                IconButton(onClick = { }) {
                    Icon(Icons.Default.ChatBubbleOutline, null, tint = Color.Gray)
                }
            }
        }
    }
}
