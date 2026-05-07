package com.gestorplus.appgestor.presentation.owner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import org.koin.compose.viewmodel.koinViewModel

// Colores del tema SoloBook
private val SoloBookBackground = Color(0xFF0F172A)
private val SoloBookSurface = Color(0xFF1E293B)
private val SoloBookPrimary = Color(0xFF3B82F6)

@Composable
fun OwnerDashboardScreen(
    viewModel: OwnerDashboardViewModel = koinViewModel()
) {
    // 1. Observamos los datos reales del ViewModel
    val bookings by viewModel.bookings.collectAsState()

    DsTheme {
        Scaffold(
            bottomBar = {
                BottomNavigationBar()
            },
            containerColor = SoloBookBackground
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                DashboardHeader()
                Spacer(modifier = Modifier.height(24.dp))
                StatusFilters()
                Spacer(modifier = Modifier.height(24.dp))
                CalendarGrid()
                Spacer(modifier = Modifier.height(32.dp))

                // 2. Pasamos las citas reales a la sección de agenda
                AgendaSection(bookings)
            }
        }
    }
}

@Composable
fun DashboardHeader() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Logo",
                    tint = SoloBookPrimary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SoloBook",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row {
                IconButton(onClick = { /* TODO */ }) {
                    Icon(Icons.Default.DateRange, "Calendar", tint = Color.White)
                }
                IconButton(onClick = { /* TODO */ }) {
                    Icon(Icons.Default.Settings, "Settings", tint = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.clickable { /* TODO */ },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "October 2023",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.LightGray)
            }
            Row {
                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.size(36.dp).background(SoloBookSurface, RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.Default.KeyboardArrowLeft, null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.size(36.dp).background(SoloBookSurface, RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.Default.KeyboardArrowRight, null, tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun StatusFilters() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip("CONFIRMED", Icons.Default.CheckCircle, true, Modifier.weight(1f))
        FilterChip("PENDING", Icons.Default.Notifications, false, Modifier.weight(1f))
        FilterChip("BLOCKED", Icons.Default.Lock, false, Modifier.weight(1f))
    }
}

@Composable
fun FilterChip(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, modifier: Modifier) {
    Surface(
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) SoloBookPrimary else SoloBookSurface
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(label, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CalendarGrid() {
    val daysOfWeek = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
    Surface(
        color = SoloBookSurface.copy(alpha = 0.5f),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                daysOfWeek.forEach { day ->
                    Text(day, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.height(260.dp),
                userScrollEnabled = false
            ) {
                items(31) { index ->
                    val day = index + 1
                    CalendarDayItem(day, day == 5, day % 7 == 3)
                }
            }
        }
    }
}

@Composable
fun CalendarDayItem(day: Int, isSelected: Boolean, hasAppointments: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(vertical = 4.dp)) {
        Box(
            modifier = Modifier.size(36.dp).background(if (isSelected) SoloBookPrimary else Color.Transparent, androidx.compose.foundation.shape.CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(day.toString(), color = Color.White, fontSize = 14.sp)
        }
        if (hasAppointments && !isSelected) {
            Box(Modifier.padding(top = 2.dp).size(4.dp).background(SoloBookPrimary, androidx.compose.foundation.shape.CircleShape))
        }
    }
}

@Composable
fun AgendaSection(bookings: List<com.gestorplus.appgestor.data.local.entity.BookingEntity>) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Agenda • Today", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Block Day", color = SoloBookPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (bookings.isEmpty()) {
            Text("No hay citas hoy", color = Color.Gray, modifier = Modifier.padding(24.dp))
        } else {
            bookings.forEach { booking ->
                AgendaItem(
                    time = "10:00 AM", // Podríamos formatear el timestamp después
                    title = booking.serviceName,
                    subtitle = "${booking.clientName} • ${booking.durationMinutes} min",
                    statusIcon = if (booking.status == "CONFIRMED") Icons.Default.CheckCircle else Icons.Default.Notifications,
                    statusColor = if (booking.status == "CONFIRMED") SoloBookPrimary else Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Ítem de Personal Time al final
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Transparent,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f))
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(40.dp).background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Lock, null, tint = Color.Gray)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("PERSONAL TIME", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Unavailable for booking", color = Color.Gray, fontSize = 12.sp)
                }
                Icon(Icons.Default.MoreVert, null, tint = Color.Gray)
            }
        }
    }
}

@Composable
fun AgendaItem(time: String, title: String, subtitle: String, statusIcon: androidx.compose.ui.graphics.vector.ImageVector, statusColor: Color) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = SoloBookSurface), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            val parts = time.split(" ")
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(parts[0], color = SoloBookPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                if (parts.size > 1) Text(parts[1], color = SoloBookPrimary.copy(alpha = 0.7f), fontSize = 10.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(subtitle, color = Color.Gray, fontSize = 12.sp)
            }
            Icon(statusIcon, null, tint = statusColor, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
fun BottomNavigationBar() {
    Surface(color = SoloBookBackground, border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f))) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceAround) {
            NavigationItem(Icons.Default.DateRange, "Calendar", true)
            NavigationItem(Icons.Default.Person, "Clients", false)
            NavigationItem(Icons.Default.Info, "Insights", false)
            NavigationItem(Icons.Default.AccountCircle, "Profile", false)
        }
    }
}

@Composable
fun NavigationItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean) {
    val color = if (isSelected) SoloBookPrimary else Color.Gray
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { /* TODO */ }) {
        Icon(icon, label, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = color, fontSize = 10.sp)
    }
}
