package com.gestorplus.appgestor.booking.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentSuccessScreen(
    onFinish: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(20.dp))
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Success Illustration/Circle
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
                        .background(AccentBlue.copy(alpha = 0.2f))
                )
                // Small orange dot like in the image
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 15.dp, end = 15.dp)
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF78166))
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                "¡Cita Enviada!",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                "Tu consulta con la Dra. Elena Martinez ha sido enviada. Te notificaremos en cuanto el doctor la confirme.",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Card(
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        "Puedes ver el estado de tu reserva y el recordatorio en tu calendario.",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Button(
                onClick = onFinish,
                modifier = Modifier.fillMaxWidth(0.6f).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cerrar", color = Color.White)
            }
        }
    }
}
