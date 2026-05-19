package com.gestorplus.appgestor.booking.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.components.input.BasicInput
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import org.jetbrains.compose.resources.stringResource
import app_gestor.composeapp.generated.resources.*

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
                    title = {
                        Text(
                            text = stringResource(Res.string.confirmation_title),
                            style = AppTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack, 
                                contentDescription = null, 
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            bottomBar = {
                ConfirmationBottomNavigation()
            },
            containerColor = AppTheme.colors.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Main Banner Card (Match Image 4)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .clip(RoundedCornerShape(24.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Background Gradient Overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent),
                                        startY = 0f, endY = 400f
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .align(Alignment.BottomStart)
                        ) {
                            Surface(
                                color = AppTheme.colors.primary.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = stringResource(Res.string.confirmation_premium_service),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    color = AppTheme.colors.primary,
                                    style = AppTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = stringResource(Res.string.confirmation_service_name),
                                color = Color.White,
                                style = AppTheme.typography.headlineMedium.copy(fontSize = 22.sp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Doctor Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF334155)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White.copy(alpha = 0.4f))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = stringResource(Res.string.confirmation_specialist_label),
                            color = Color(0xFF94A3B8),
                            style = AppTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        )
                        Text(
                            text = stringResource(Res.string.confirmation_doctor_name),
                            color = Color.White,
                            style = AppTheme.typography.headlineSmall.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Date and Time Section
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(Res.string.confirmation_label_fecha),
                                color = Color(0xFF94A3B8),
                                style = AppTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Text(
                            text = "Oct 5, 2023",
                            modifier = Modifier.padding(start = 28.dp),
                            color = Color.White,
                            style = AppTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(Res.string.confirmation_label_hora),
                                color = Color(0xFF94A3B8),
                                style = AppTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Text(
                            text = "10:00 AM",
                            modifier = Modifier.padding(start = 28.dp),
                            color = Color.White,
                            style = AppTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Notes Input Section
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Notes, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(Res.string.confirmation_notes_label),
                        color = Color.White,
                        style = AppTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                BasicInput(
                    value = notes,
                    onValueChange = { notes = it },
                    label = "",
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    singleLine = false,
                    minLines = 4
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Price Section
                PriceItem(stringResource(Res.string.confirmation_service_price_1), "$80.00")
                Spacer(modifier = Modifier.height(12.dp))
                PriceItem(stringResource(Res.string.confirmation_service_price_2), "$45.00")

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun PriceItem(label: String, price: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color(0xFF94A3B8),
            style = AppTheme.typography.bodyMedium
        )
        Text(
            text = price,
            color = Color.White,
            style = AppTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun ConfirmationBottomNavigation() {
    Surface(
        color = AppTheme.colors.background,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ConfirmationNavItem(Icons.Default.Home, stringResource(Res.string.nav_inicio), false)
            ConfirmationNavItem(Icons.Default.CalendarMonth, stringResource(Res.string.nav_citas), true)
            ConfirmationNavItem(Icons.Default.Person, stringResource(Res.string.nav_perfil), false)
            ConfirmationNavItem(Icons.Default.Settings, stringResource(Res.string.nav_ajustes), false)
        }
    }
}

@Composable
private fun ConfirmationNavItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean) {
    val color = if (isSelected) Color(0xFF3B82F6) else Color(0xFF94A3B8)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = { /* Nav Action */ })
    ) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = color, style = AppTheme.typography.labelSmall)
    }
}
