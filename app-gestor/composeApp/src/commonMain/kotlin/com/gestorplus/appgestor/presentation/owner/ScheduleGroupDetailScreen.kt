package com.gestorplus.appgestor.presentation.owner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.DsTheme

private val DarkBackground = Color(0xFF0F172A)
private val SurfaceColor = Color(0xFF1E293B)
private val PrimaryBlue = Color(0xFF3B82F6)
private val InfoCardBackground = Color(0xFF162033)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleGroupDetailScreen(
    onBack: () -> Unit
) {
    DsTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Working Hours", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(Icons.Default.MoreVert, null, tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBackground)
                )
            },
            containerColor = DarkBackground,
            bottomBar = {
                BottomNavigationBarPlaceholder()
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Monday — Friday Group",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                GroupExplanationCard()
                
                Spacer(modifier = Modifier.height(24.dp))
                
                val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
                days.forEach { day ->
                    GroupDayItem(day)
                    Spacer(modifier = Modifier.height(12.dp))
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Surface(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth().clickable { /* TODO */ }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.AddCircleOutline, null, tint = PrimaryBlue)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Add Group Exception", color = PrimaryBlue, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Break link to master to set specific times",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun GroupExplanationCard() {
    Surface(
        color = InfoCardBackground,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.Info, null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "These days are currently following the Master Schedule (09:00 AM - 05:00 PM). Changes to the Master Schedule will automatically update these days.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun GroupDayItem(day: String) {
    Surface(
        color = SurfaceColor.copy(alpha = 0.5f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(day, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(color = PrimaryBlue.copy(alpha = 0.1f), shape = RoundedCornerShape(20.dp)) {
                        Text(
                            "FOLLOWING MASTER", 
                            color = PrimaryBlue.copy(alpha = 0.6f), 
                            fontSize = 9.sp, 
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("09:00 AM - 05:00 PM", color = Color.White, fontSize = 14.sp)
            }
            
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .clickable { /* TODO */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.EditCalendar, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun BottomNavigationBarPlaceholder() {
    Surface(color = DarkBackground, border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f))) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceAround) {
            NavigationItem(Icons.Default.CalendarToday, "Schedule", true)
            NavigationItem(Icons.Default.Assignment, "Bookings", false)
            NavigationItem(Icons.Default.People, "Clients", false)
            NavigationItem(Icons.Default.Settings, "Settings", false)
        }
    }
}

@Composable
private fun NavigationItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean) {
    val color = if (isSelected) PrimaryBlue else Color.Gray
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, label, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = color, fontSize = 10.sp)
    }
}
