package com.gestorplus.appgestor.owner.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.MedicalServices
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.owner.home.presentation.composable.AppointmentTimelineItem
import com.gestorplus.appgestor.owner.home.presentation.composable.MetricCard
import com.gestorplus.appgestor.owner.home.presentation.composable.NextAppointmentCard
import com.gestorplus.appgestor.owner.home.presentation.state.OwnerHomeEffect
import com.gestorplus.appgestor.owner.home.presentation.state.OwnerHomeEvent
import com.gestorplus.appgestor.owner.home.presentation.viewmodel.OwnerHomeViewModel
import org.koin.compose.viewmodel.koinViewModel

// ── Design Tokens ──────────────────────────────────────────────────────
private val DeepNavy = Color(0xFF0B1120)
private val CardBg = Color(0xFF1A2332)
private val GlassBorder = Color(0xFF2A3A4A)
private val BrandBlue = Color(0xFF3B82F6)
private val AccentGreen = Color(0xFF22C55E)
private val SoftWhite = Color(0xFFF1F5F9)

@Composable
fun OwnerHomeScreen(
    viewModel: OwnerHomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is OwnerHomeEffect.ShowError -> { /* TO-DO: show snackbar */ }
                else -> { /* Navigation effects - handled by parent */ }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(OwnerHomeEvent.OnAddAppointmentClicked) },
                containerColor = BrandBlue,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar cita",
                    tint = Color.White
                )
            }
        },
        containerColor = DeepNavy
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BrandBlue)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // ── Top Bar ──────────────────────────────────────────
                TopBarSection(doctorName = state.doctorName)

                Spacer(modifier = Modifier.height(24.dp))

                // ── Greeting ─────────────────────────────────────────
                Text(
                    text = "Buenos días,",
                    color = SoftWhite.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
                Text(
                    text = state.doctorName,
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Quick Stats ──────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        label = "CITAS",
                        value = state.totalAppointments.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        label = "PENDIENTES",
                        value = state.pendingAppointments.toString(),
                        showBadge = state.pendingAppointments > 0,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Quick Links ──────────────────────────────────────
                QuickLinkCard(
                    icon = Icons.Default.MedicalServices,
                    iconTint = AccentGreen,
                    title = "Catálogo de Servicios",
                    subtitle = "Gestiona tus consultas y procedimientos",
                    onClick = { viewModel.onEvent(OwnerHomeEvent.OnServiceCatalogClicked) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                QuickLinkCard(
                    icon = Icons.Default.Schedule,
                    iconTint = BrandBlue,
                    title = "Configuración de Turnos",
                    subtitle = "Gestiona tus horarios y disponibilidad",
                    onClick = { viewModel.onEvent(OwnerHomeEvent.OnShiftConfigClicked) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ── Next Appointment ─────────────────────────────────
                state.nextAppointment?.let { next ->
                    Text(
                        text = "PRÓXIMA CITA",
                        color = SoftWhite.copy(alpha = 0.5f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    NextAppointmentCard(
                        appointment = next,
                        onStartConsultation = {
                            viewModel.onEvent(OwnerHomeEvent.OnStartConsultationClicked)
                        },
                        onChatClicked = {
                            viewModel.onEvent(OwnerHomeEvent.OnChatClicked(next.id))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Rest of the Day ──────────────────────────────────
                if (state.restOfDayAppointments.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "RESTO DEL DÍA",
                            color = SoftWhite.copy(alpha = 0.5f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Ver Todo",
                            color = BrandBlue,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                viewModel.onEvent(OwnerHomeEvent.OnViewAllClicked)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    state.restOfDayAppointments.forEachIndexed { index, appointment ->
                        AppointmentTimelineItem(
                            appointment = appointment,
                            isLast = index == state.restOfDayAppointments.lastIndex
                        )
                    }
                }

                Spacer(modifier = Modifier.height(80.dp)) // FAB clearance
            }
        }
    }
}

// ── Internal Composables ──────────────────────────────────────────────

@Composable
private fun TopBarSection(doctorName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Profile avatar
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(BrandBlue, AccentGreen)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = doctorName.split(" ").lastOrNull()?.firstOrNull()?.toString() ?: "D",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "SoloBook Pro",
                color = BrandBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        // Notification bell
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(CardBg)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notificaciones",
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun QuickLinkCard(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 12.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.3f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
