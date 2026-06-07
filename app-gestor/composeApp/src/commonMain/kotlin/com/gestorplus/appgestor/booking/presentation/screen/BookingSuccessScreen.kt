package com.gestorplus.appgestor.booking.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DarkBackground = Color(0xFF0D1117)
private val CardBackground = Color(0xFF161B22)
private val AccentBlue = Color(0xFF2F81F7)
private val LightBlue = Color(0xFF93C5FD)
private val TextGray = Color(0xFF8B949E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSuccessScreen(
    onNavigateToMyBookings: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = LightBlue, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SoloBook", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Success Icon
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(AccentBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(AccentBlue.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = AccentBlue,
                        modifier = Modifier.size(48.dp)
                    )
                }
                
                // Small orange dot
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF97316))
                        .align(Alignment.TopEnd)
                        .offset(x = (-20).dp, y = 20.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "¡Cita Enviada!",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Tu consulta con la Dra. Elena Martínez ha sido enviada. Te notificaremos en cuanto el doctor la confirme.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Info Box
            Surface(
                color = CardBackground,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = TextGray, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Puedes ver el estado de tu reserva y el recordatorio en tu calendario.",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Buttons
            Button(
                onClick = onNavigateToMyBookings,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFF1E3A8A), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver en mi Calendario", color = Color(0xFF1E3A8A), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TextButton(onClick = onNavigateToHome) {
                Text("Volver al Inicio", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
