package com.gestorplus.appgestor.my_bookings.presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.gestorplus.appgestor.my_bookings.domain.model.BookingStatus
import com.gestorplus.appgestor.my_bookings.domain.model.PatientBooking
import org.koin.compose.viewmodel.koinViewModel
import kotlinx.datetime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookingsScreen(
    onNavigateToExplore: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: MyBookingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    DsTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MedicalServices,
                                contentDescription = null,
                                tint = AppTheme.colors.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Mis Reservas",
                                style = AppTheme.typography.headlineLarge.copy(
                                    fontSize = 18.sp,
                                    color = AppTheme.colors.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Notificaciones */ }) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = AppTheme.colors.textSecondary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AppTheme.colors.background
                    )
                )
            },
            bottomBar = {
                com.gestorplus.appgestor.explore_clinics.presentation.BottomNavigationBar(
                    currentScreen = "Mis Citas",
                    onExplorarClick = onNavigateToExplore,
                    onCitasClick = { },
                    onPerfilClick = onNavigateToProfile
                )
            },
            containerColor = AppTheme.colors.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                // Filter Tabs
                BookingFilterTabs(
                    selectedTab = state.selectedTab,
                    onTabSelected = viewModel::onTabSelected
                )

                Spacer(modifier = Modifier.height(24.dp))

                val currentBookings = if (state.selectedTab == BookingTab.UPCOMING) {
                    state.upcomingBookings
                } else {
                    state.pastBookings
                }

                if (state.isLoading && currentBookings.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AppTheme.colors.primary)
                    }
                } else if (currentBookings.isEmpty()) {
                    EmptyBookingsState()
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    if (state.selectedTab == BookingTab.UPCOMING) "Reciente" else "Anteriores",
                                    style = AppTheme.typography.headlineLarge.copy(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AppTheme.colors.textPrimary
                                    )
                                )
                                if (state.selectedTab == BookingTab.UPCOMING && currentBookings.any { it.status == BookingStatus.PENDING || it.status == BookingStatus.REVIEWING }) {
                                    Surface(
                                        color = Color(0xFFFFB779).copy(alpha = 0.1f),
                                        shape = CircleShape,
                                        border = BorderStroke(1.dp, Color(0xFFFFB779).copy(alpha = 0.2f))
                                    ) {
                                        Text(
                                            "Pendiente de Confirmación",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            style = AppTheme.typography.labelLarge.copy(
                                                fontSize = 9.sp,
                                                color = Color(0xFFFFB779),
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        items(currentBookings) { booking ->
                            BookingCard(booking = booking)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookingFilterTabs(selectedTab: BookingTab, onTabSelected: (BookingTab) -> Unit) {
    Surface(
        color = AppTheme.colors.surface.copy(alpha = 0.3f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().height(48.dp)
    ) {
        Row(modifier = Modifier.padding(4.dp)) {
            BookingTabItem(
                label = "Próximas",
                isSelected = selectedTab == BookingTab.UPCOMING,
                onClick = { onTabSelected(BookingTab.UPCOMING) },
                modifier = Modifier.weight(1f)
            )
            BookingTabItem(
                label = "Historial",
                isSelected = selectedTab == BookingTab.PAST,
                onClick = { onTabSelected(BookingTab.PAST) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun BookingTabItem(label: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) AppTheme.colors.surface else Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            style = AppTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textSecondary
            )
        )
    }
}

@Composable
fun BookingCard(booking: PatientBooking) {
    Surface(
        color = AppTheme.colors.surface.copy(alpha = 0.6f),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, AppTheme.colors.primary.copy(alpha = 0.05f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AsyncImage(
                    model = booking.doctorImageUrl ?: "https://lh3.googleusercontent.com/aida-public/AB6AXuDXguQKKUCFDhIZ5K4VXxHO3H_kRvMgeVnV_Kc-ms-Vbo-WRH03YoJxJzYlwc3Of188m1lcs2ICKMX9FUPXd3akUbdjYQlrvwO57I6RfLtzpQfh7y0J6DH-Xjcgv7uGdPR7OzzcQ8m870IaX_WI_ESboE0tos8-oL3nbxO_v4uy1HX_JZAh3jXyMk6ArCrz8iCFigmOPMDQeU_e8Ijl6fcSy3THkIAho45FGjNgJmSrAkupw9XNT0kmf4VSycTw1zsw0TDx2UGfqcQ",
                    contentDescription = null,
                    modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        booking.clinicName, // Cambiado de doctorName a clinicName para rescatar el dato correcto
                        style = AppTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.textPrimary
                        )
                    )
                    Text(
                        booking.serviceName,
                        style = AppTheme.typography.labelLarge.copy(color = AppTheme.colors.textSecondary, fontSize = 12.sp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val dateText = "${booking.month?.take(3) ?: "Oct"} ${booking.date ?: 24}"
                    val timeText = booking.timeSlot ?: "10:00 AM"

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarMonth, null, tint = AppTheme.colors.primary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(dateText, style = AppTheme.typography.labelLarge.copy(color = AppTheme.colors.primary, fontWeight = FontWeight.Bold, fontSize = 12.sp))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, null, tint = AppTheme.colors.primary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(timeText, style = AppTheme.typography.labelLarge.copy(color = AppTheme.colors.primary, fontWeight = FontWeight.Bold, fontSize = 12.sp))
                        }
                    }
                }
            }

            // Progress Tracker
            BookingProgressTracker(status = booking.status)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.surface.copy(alpha = 0.3f))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Ver detalles",
                    style = AppTheme.typography.labelLarge.copy(color = AppTheme.colors.primary, fontWeight = FontWeight.Bold, fontSize = 12.sp),
                    modifier = Modifier.clickable { /* Ver detalles */ }
                )
                if (booking.status == BookingStatus.PENDING || booking.status == BookingStatus.REVIEWING) {
                    Text(
                        "Cancelar",
                        style = AppTheme.typography.labelLarge.copy(color = AppTheme.colors.error, fontWeight = FontWeight.Bold, fontSize = 12.sp),
                        modifier = Modifier.clickable { /* Cancelar */ }
                    )
                }
            }
        }
    }
}

@Composable
fun BookingProgressTracker(status: BookingStatus) {
    val progress = when(status) {
        BookingStatus.PENDING -> 0.25f
        BookingStatus.REVIEWING -> 0.65f
        BookingStatus.CONFIRMED -> 1f
        else -> 0f
    }

    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            // Background Line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(AppTheme.colors.textSecondary.copy(alpha = 0.1f))
            )
            // Progress Line
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(2.dp)
                    .background(AppTheme.colors.primary)
            )

            // Steps
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ProgressStep(
                    label = "Solicitud", 
                    isDone = true, 
                    isActive = false, 
                    icon = Icons.Default.Check
                )
                ProgressStep(
                    label = "En Revisión", 
                    isDone = progress >= 0.65f, 
                    isActive = status == BookingStatus.REVIEWING, 
                    icon = if (status == BookingStatus.REVIEWING) Icons.Default.Sync else Icons.Default.Check
                )
                ProgressStep(
                    label = "Confirmada", 
                    isDone = progress == 1f, 
                    isActive = status == BookingStatus.CONFIRMED, 
                    icon = Icons.Default.EventAvailable
                )
            }
        }
    }
}

@Composable
fun ProgressStep(label: String, isDone: Boolean, isActive: Boolean, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isDone || isActive) AppTheme.colors.primary else AppTheme.colors.surface)
                .border(1.dp, if (isDone || isActive) AppTheme.colors.primary else AppTheme.colors.textSecondary.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isDone || isActive) Color.White else AppTheme.colors.textSecondary.copy(alpha = 0.6f),
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            label,
            style = AppTheme.typography.labelLarge.copy(
                fontSize = 10.sp,
                color = if (isDone || isActive) AppTheme.colors.primary else AppTheme.colors.textSecondary.copy(alpha = 0.6f),
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
fun EmptyBookingsState() {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = null,
            tint = AppTheme.colors.textSecondary.copy(alpha = 0.1f),
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "No tienes reservas",
            style = AppTheme.typography.bodyMedium.copy(
                color = AppTheme.colors.textSecondary,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
