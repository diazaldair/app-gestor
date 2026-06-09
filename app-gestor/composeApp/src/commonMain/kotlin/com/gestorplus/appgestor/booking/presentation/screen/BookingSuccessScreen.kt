package com.gestorplus.appgestor.booking.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.designsystem.theme.DsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSuccessScreen(
    clinicName: String,
    doctorImageUrl: String?,
    onGoHome: () -> Unit,
    onViewCalendar: () -> Unit
) {
    DsTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = AppTheme.colors.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "SoloBook",
                                style = AppTheme.typography.headlineLarge.copy(
                                    fontSize = 18.sp,
                                    color = AppTheme.colors.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    },
                    actions = {
                        Box(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(32.dp)
                                .clip(CircleShape)
                                .border(1.dp, AppTheme.colors.primary.copy(alpha = 0.2f), CircleShape)
                        ) {
                            AsyncImage(
                                model = doctorImageUrl ?: "https://lh3.googleusercontent.com/aida-public/AB6AXuCb341N9WPFDOjy0HupWRQ-FonKDNY651Y212sB3G8SvErBJqfO5UOaWFWpNgWaiDMTHORu91NTQDjzC3aKfqefwvyV1qI7mSfhUPI7zBDsiMqE8rlJzfG2GSN-5FKPjz9q-vgrkXfqa2bPm2x_Zgl9IxU98pTBpDZiRF3mjFqpIIt1ibkV7CzzwW3KGCv4JUWtYy2xa7MEEop6h6AaLpdx71Mo0RVyqIcyFlI_g2TMLb6o4FUX9-IZAiXNbyWbBHc6YNt9Rhe4ELw",
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = AppTheme.colors.background
                    )
                )
            },
            bottomBar = {
                SuccessFooter(onGoHome = onGoHome, onViewCalendar = onViewCalendar)
            },
            containerColor = AppTheme.colors.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                // Animated Success Indicator (Glow effect simulated with background)
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(AppTheme.colors.primary.copy(alpha = 0.1f))
                    )
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(AppTheme.colors.primary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = AppTheme.colors.primary,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    "¡Cita Enviada!",
                    style = AppTheme.typography.headlineLarge.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.textPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Tu consulta con $clinicName ha sido enviada. Te notificaremos en cuanto se confirme.",
                    style = AppTheme.typography.bodyMedium.copy(
                        color = AppTheme.colors.textSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    ),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Info card
                Surface(
                    color = AppTheme.colors.surface.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = AppTheme.colors.accent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Puedes ver el estado de tu reserva y el recordatorio en tu calendario.",
                            style = AppTheme.typography.labelLarge.copy(
                                fontSize = 12.sp,
                                color = AppTheme.colors.textSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SuccessFooter(onGoHome: () -> Unit, onViewCalendar: () -> Unit) {
    Surface(
        color = AppTheme.colors.surface.copy(alpha = 0.9f),
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .padding(bottom = 16.dp)
        ) {
            Button(
                onClick = onViewCalendar,
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarMonth, null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver en mi Calendario", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            TextButton(
                onClick = onGoHome,
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text(
                    "Volver al Inicio",
                    style = AppTheme.typography.bodyMedium.copy(
                        color = AppTheme.colors.primary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
