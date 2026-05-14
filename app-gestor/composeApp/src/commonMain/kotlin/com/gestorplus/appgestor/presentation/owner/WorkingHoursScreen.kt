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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.DsTheme

private val DarkBackground = Color(0xFF0F172A)
private val SurfaceColor = Color(0xFF1E293B)
private val PrimaryBlue = Color(0xFF3B82F6)
private val CardBorderColor = Color(0xFF334155)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkingHoursScreen(
    onBack: () -> Unit,
    onNavigateToGroupDetail: () -> Unit
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
                Box(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = { onBack() },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Icon(Icons.Default.Save, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save All Changes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
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
                
                MasterScheduleSection()
                
                Spacer(modifier = Modifier.height(24.dp))
                
                AutoLunchSection()
                
                Spacer(modifier = Modifier.height(24.dp))
                
                CurrentScheduleHeader()
                
                Spacer(modifier = Modifier.height(16.dp))
                
                GroupScheduleItem(
                    days = listOf("M", "W", "F"), 
                    label = "Mon — Fri (Avg)", 
                    status = "FOLLOWING MASTER",
                    onClick = onNavigateToGroupDetail
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                DayScheduleItem("Saturday", status = "CLOSED FOR BUSINESS", isEnabled = false)
                DayScheduleItem("Sunday", status = "DAY OFF", isEnabled = false)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                TimingDefaultsSection()
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun MasterScheduleSection() {
    Surface(
        color = SurfaceColor.copy(alpha = 0.5f),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Master Schedule", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Surface(color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                    Text("DEFAULT", color = Color.Gray, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TimeInputBox("START", "09:00 AM", modifier = Modifier.weight(1f))
                TimeInputBox("END", "05:00 PM", modifier = Modifier.weight(1f))
                Box(modifier = Modifier.size(48.dp).background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.ContentCopy, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Apply this rule to:", color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                    val isSelected = day !in listOf("Sat", "Sun")
                    Surface(
                        color = if (isSelected) PrimaryBlue.copy(alpha = 0.2f) else Color.Transparent,
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) PrimaryBlue else Color.Gray.copy(alpha = 0.3f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = day,
                            color = if (isSelected) PrimaryBlue else Color.Gray,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GroupScheduleItem(
    days: List<String>,
    label: String,
    status: String,
    onClick: () -> Unit
) {
    Surface(
        color = SurfaceColor.copy(alpha = 0.3f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
                days.forEach { day ->
                    Box(
                        modifier = Modifier.size(32.dp).background(PrimaryBlue, androidx.compose.foundation.shape.CircleShape).border(2.dp, SurfaceColor, androidx.compose.foundation.shape.CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(day, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(status, color = PrimaryBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            
            Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.Gray)
        }
    }
}

@Composable
fun TimeInputBox(label: String, time: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Surface(
            color = Color.Gray.copy(alpha = 0.1f),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(label, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(time, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AutoLunchSection() {
    Surface(
        color = SurfaceColor.copy(alpha = 0.5f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Restaurant, null, tint = Color(0xFFF59E0B))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Auto Lunch", color = Color.White, fontWeight = FontWeight.Bold)
                Text("12:00 PM - 01:00 PM (Enabled)", color = Color.Gray, fontSize = 12.sp)
            }
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.Edit, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun CurrentScheduleHeader() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CalendarViewMonth, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Current Schedule", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Text("RESET ALL", color = PrimaryBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DayScheduleItem(
    day: String,
    start: String = "",
    end: String = "",
    status: String = "",
    isEnabled: Boolean,
    isFollowingMaster: Boolean = false,
    isCustom: Boolean = false
) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).background(if (isEnabled) PrimaryBlue else Color.Gray.copy(alpha = 0.1f), androidx.compose.foundation.shape.CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(day.take(1), color = if (isEnabled) Color.White else Color.Gray, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(day, color = Color.White, fontWeight = FontWeight.Bold)
                    if (isFollowingMaster) {
                        Text("FOLLOWING MASTER", color = PrimaryBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    } else if (isCustom) {
                        Text("CUSTOM EXCEPTION", color = Color(0xFFF59E0B), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    } else if (status.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(6.dp).background(Color(0xFFEF4444), androidx.compose.foundation.shape.CircleShape))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(status, color = Color(0xFFEF4444).copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Switch(
                    checked = isEnabled,
                    onCheckedChange = { },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryBlue)
                )
            }
            
            if (isCustom) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = SurfaceColor.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth().padding(start = 56.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        TimeInputBox("START", start, modifier = Modifier.weight(1f))
                        TimeInputBox("END", end, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ContentCopy, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                }
                Text("Early close enabled", color = Color.Gray, fontSize = 10.sp, modifier = Modifier.padding(start = 56.dp, top = 4.dp), fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            }
        }
    }
}

@Composable
fun TimingDefaultsSection() {
    Surface(
        color = SurfaceColor.copy(alpha = 0.5f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Timer, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Timing Defaults", color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.Info, null, tint = PrimaryBlue, modifier = Modifier.size(16.dp))
            }
            Text("These are your global presets applied to all new appointments.", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TimingInput("DURATION", "60", "MIN", modifier = Modifier.weight(1f))
                TimingInput("BUFFER", "15", "MIN", modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun TimingInput(label: String, value: String, unit: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            color = Color.Gray.copy(alpha = 0.1f),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Text(value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(unit, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 2.dp))
            }
        }
    }
}
