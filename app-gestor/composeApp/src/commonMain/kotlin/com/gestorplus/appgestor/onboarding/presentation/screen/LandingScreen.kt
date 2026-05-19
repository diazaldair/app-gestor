package com.gestorplus.appgestor.onboarding.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.onboarding.presentation.state.LandingEfffect
import com.gestorplus.appgestor.onboarding.presentation.state.LandingEvent
import com.gestorplus.appgestor.onboarding.presentation.viewmodel.LandingViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

// Paleta de colores Premium de la Landing Page
private val DarkBgStart = Color(0xFF0F172A)
private val DarkBgEnd = Color(0xFF020617)
private val BrandBlue = Color(0xFF3B82F6)
private val BrandLightBlue = Color(0xFF60A5FA)
private val GlassSurface = Color(0xFF1E293B).copy(alpha = 0.4f)
private val GlassBorder = Color(0xFF334155).copy(alpha = 0.3f)
private val ProfessionalButtonBg = Color(0xFF1E293B).copy(alpha = 0.8f)

@Composable
fun LandingScreen(
    onNavigateToPatient: () -> Unit,
    onNavigateToProfessional: () -> Unit,
    viewModel: LandingViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                LandingEfffect.NavigateToPatientFlow -> onNavigateToPatient()
                LandingEfffect.NavigateToProfessionalFlow -> onNavigateToProfessional()
            }
        }
    }

    DsTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(DarkBgStart, DarkBgEnd)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header (Espacio superior e info de marca)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = BrandLightBlue,
                        modifier = Modifier
                            .size(64.dp)
                            .padding(bottom = 12.dp)
                    )
                    
                    Text(
                        text = "SoloBook Pro",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "GESTIÓN MÉDICA DE ÉLITE",
                        color = BrandLightBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Cuerpo Central (Eslogan y descripción)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tu tiempo, ",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "bajo control.",
                            color = BrandLightBlue,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Optimiza tu agenda con nuestra gestión de citas y filas virtuales en tiempo real.",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                // Selector de Perfiles (Card Glassmorphic)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(32.dp))
                        .background(GlassSurface)
                        .border(1.dp, GlassBorder, RoundedCornerShape(32.dp))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SELECCIONA TU PERFIL",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Botón Paciente (Azul Primario)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(BrandBlue)
                            .clickable { viewModel.onEvent(LandingEvent.OnPatientSelected) }
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "Soy Paciente",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón Profesional (Gris Oscuro Glass)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(ProfessionalButtonBg)
                            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                            .clickable { viewModel.onEvent(LandingEvent.OnProfessionalSelected) }
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MedicalServices,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "Soy Profesional",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Servidores Activos Indicador
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFF10B981)) // Verde activo
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = state.statusText,
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Footer (Versión e información)
                Text(
                    text = "VERSION ${state.version}  •  SECURE CONNECTED",
                    color = Color.White.copy(alpha = 0.3f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
