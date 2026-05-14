package com.gestorplus.appgestor.presentation.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
private val DoctorGreen = Color(0xFF10B981)

@Composable
fun LandingScreen(
    onNavigateToClient: () -> Unit,
    onNavigateToBusiness: () -> Unit,
    onNavigateToDoctor: () -> Unit
) {
    DsTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(DarkBackground, Color(0xFF020617))
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                
                Text(
                    text = "GestorPlus",
                    color = PrimaryBlue,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = "Tu ecosistema de gestión inteligente",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                Text(
                    text = "Seleccione su perfil para comenzar",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp, bottom = 48.dp)
                )

                NavigationCard(
                    title = "Vista Cliente",
                    description = "Agenda tu próxima cita en segundos",
                    icon = Icons.Default.Person,
                    color = PrimaryBlue,
                    onClick = onNavigateToClient
                )

                Spacer(modifier = Modifier.height(20.dp))

                NavigationCard(
                    title = "Vista Negocio",
                    description = "Control total de tu agenda y personal",
                    icon = Icons.Default.BusinessCenter,
                    color = SurfaceColor,
                    onClick = onNavigateToBusiness
                )

                Spacer(modifier = Modifier.height(20.dp))

                NavigationCard(
                    title = "Vista Doctor",
                    description = "Gestión de pacientes y horarios médicos",
                    icon = Icons.Default.MedicalServices,
                    color = DoctorGreen,
                    onClick = onNavigateToDoctor
                )
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun NavigationCard(
    title: String,
    description: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF1E293B).copy(alpha = 0.6f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(16.dp),
                color = color.copy(alpha = 0.2f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (color == SurfaceColor) Color.White else color,
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    color = Color.Gray,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
            
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color.Gray.copy(alpha = 0.5f)
            )
        }
    }
}
