package com.gestorplus.appgestor.owner.dashboard.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.core.util.DateTimeUtils
import com.gestorplus.appgestor.owner.dashboard.domain.model.Booking
import com.gestorplus.appgestor.owner.dashboard.presentation.viewmodel.OwnerDashboardViewModel
import com.gestorplus.appgestor.owner.dashboard.presentation.state.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import androidx.compose.foundation.lazy.grid.items
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.resources.stringResource
import app_gestor.composeapp.generated.resources.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Instant

@Composable
fun OwnerDashboardScreen(
    onBack: () -> Unit,
    onNavigateToWorkingHours: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToServices: () -> Unit,
    viewModel: OwnerDashboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val bookings by viewModel.bookings.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is OwnerDashboardEfffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    DsTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                BottomNavigationBar(
                    onNavigateToProfile = onNavigateToProfile,
                    currentScreen = "Calendario"
                )
            },
            containerColor = AppTheme.colors.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                DashboardHeader(
                    selectedDate = selectedDate,
                    onMonthChange = { viewModel.onEvent(OwnerDashboardEvent.OnMonthChange(it)) },
                    onBack = onBack,
                    onNavigateToWorkingHours = onNavigateToWorkingHours,
                    onNavigateToServices = onNavigateToServices
                )
                Spacer(modifier = Modifier.height(24.dp))
                StatusFilters(
                    selectedFilter = state.selectedFilter,
                    onFilterChanged = { viewModel.onEvent(OwnerDashboardEvent.OnFilterChanged(it)) }
                )
                Spacer(modifier = Modifier.height(24.dp))
                CalendarGrid(
                    selectedDate = selectedDate,
                    allBookings = state.bookings,
                    onDateSelected = { viewModel.onEvent(OwnerDashboardEvent.OnDateSelected(it)) }
                )
                Spacer(modifier = Modifier.height(32.dp))

                if (state.isSyncing) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = AppTheme.colors.primary,
                        trackColor = AppTheme.colors.surface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                AgendaSection(
                    bookings = bookings,
                    onAccept = { viewModel.onEvent(OwnerDashboardEvent.OnAcceptBooking(it)) },
                    onReject = { viewModel.onEvent(OwnerDashboardEvent.OnRejectBooking(it)) }
                )
            }
        }
    }
}

@Composable
fun DashboardHeader(
    selectedDate: LocalDate,
    onMonthChange: (Int) -> Unit,
    onBack: () -> Unit,
    onNavigateToWorkingHours: () -> Unit,
    onNavigateToServices: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(Res.string.common_back),
                        tint = AppTheme.colors.textPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(Res.string.app_name),
                    tint = AppTheme.colors.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(Res.string.app_name),
                    color = AppTheme.colors.textPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row {
                IconButton(onClick = onNavigateToWorkingHours) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = stringResource(Res.string.common_settings),
                        tint = AppTheme.colors.primary
                    )
                }
                IconButton(onClick = onNavigateToServices) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = "Services",
                        tint = AppTheme.colors.textPrimary
                    )
                }
                IconButton(onClick = { /* TODO: Event */ }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(Res.string.common_settings),
                        tint = AppTheme.colors.textPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.clickable { /* TODO */ },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${selectedDate.month.name} ${selectedDate.year}",
                    color = AppTheme.colors.textPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(Icons.Default.KeyboardArrowDown, null, tint = AppTheme.colors.textSecondary)
            }
            Row {
                IconButton(
                    onClick = { onMonthChange(-1) },
                    modifier = Modifier.size(36.dp).background(AppTheme.colors.surface, RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = stringResource(Res.string.common_prev),
                        tint = AppTheme.colors.textPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { onMonthChange(1) },
                    modifier = Modifier.size(36.dp).background(AppTheme.colors.surface, RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = stringResource(Res.string.common_next),
                        tint = AppTheme.colors.textPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun StatusFilters(
    selectedFilter: BookingFilter,
    onFilterChanged: (BookingFilter) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            label = stringResource(Res.string.status_confirmed),
            icon = Icons.Default.CheckCircle,
            isSelected = selectedFilter == BookingFilter.CONFIRMED,
            modifier = Modifier.weight(1f),
            onClick = { onFilterChanged(if (selectedFilter == BookingFilter.CONFIRMED) BookingFilter.ALL else BookingFilter.CONFIRMED) }
        )
        FilterChip(
            label = stringResource(Res.string.status_pending),
            icon = Icons.Default.Notifications,
            isSelected = selectedFilter == BookingFilter.PENDING,
            modifier = Modifier.weight(1f),
            onClick = { onFilterChanged(if (selectedFilter == BookingFilter.PENDING) BookingFilter.ALL else BookingFilter.PENDING) }
        )
        FilterChip(
            label = stringResource(Res.string.status_blocked),
            icon = Icons.Default.Lock,
            isSelected = selectedFilter == BookingFilter.BLOCKED,
            modifier = Modifier.weight(1f),
            onClick = { onFilterChanged(if (selectedFilter == BookingFilter.BLOCKED) BookingFilter.ALL else BookingFilter.BLOCKED) }
        )
    }
}

@Composable
fun FilterChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.height(40.dp).clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.surface
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) AppTheme.colors.onPrimary else AppTheme.colors.textPrimary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                color = if (isSelected) AppTheme.colors.onPrimary else AppTheme.colors.textPrimary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CalendarGrid(
    selectedDate: LocalDate,
    allBookings: List<Booking>,
    onDateSelected: (Int) -> Unit
) {
    val daysOfWeek = listOf(
        Res.string.day_sun,
        Res.string.day_mon,
        Res.string.day_tue,
        Res.string.day_wed,
        Res.string.day_thu,
        Res.string.day_fri,
        Res.string.day_sat
    )
    
    val firstDayOfMonth = LocalDate(selectedDate.year, selectedDate.month, 1)
    val daysInMonth = when (selectedDate.month) {
        kotlinx.datetime.Month.FEBRUARY -> if (selectedDate.year % 4 == 0) 29 else 28
        kotlinx.datetime.Month.APRIL, kotlinx.datetime.Month.JUNE, kotlinx.datetime.Month.SEPTEMBER, kotlinx.datetime.Month.NOVEMBER -> 30
        else -> 31
    }
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.isoDayNumber % 7 // 0=Sun, 1=Mon...

    Surface(
        color = AppTheme.colors.surface.copy(alpha = 0.5f),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                daysOfWeek.forEach { dayRes ->
                    Text(
                        text = stringResource(dayRes),
                        color = AppTheme.colors.textSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.height(260.dp),
                userScrollEnabled = false
            ) {
                items(firstDayOfWeek) {
                    Spacer(Modifier.size(36.dp))
                }
                
                items(daysInMonth) { index ->
                    val day = index + 1
                    val date = LocalDate(selectedDate.year, selectedDate.month, day)
                    val hasAppointments = allBookings.any {
                        val bDate = Instant.fromEpochMilliseconds(it.timestamp).toLocalDateTime(TimeZone.currentSystemDefault()).date
                        bDate == date
                    }

                    CalendarDayItem(
                        day = day, 
                        isSelected = day == selectedDate.dayOfMonth, 
                        hasAppointments = hasAppointments, 
                        onSelect = { onDateSelected(day) }
                    )
                }
            }
        }
    }
}

@Composable
fun CalendarDayItem(day: Int, isSelected: Boolean, hasAppointments: Boolean, onSelect: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, 
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clickable { onSelect() }
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    if (isSelected) AppTheme.colors.primary else Color.Transparent,
                    RoundedCornerShape(18.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.toString(),
                color = if (isSelected) AppTheme.colors.onPrimary else AppTheme.colors.textPrimary,
                fontSize = 14.sp
            )
        }
        if (hasAppointments && !isSelected) {
            Box(
                Modifier
                    .padding(top = 2.dp)
                    .size(4.dp)
                    .background(AppTheme.colors.primary, RoundedCornerShape(2.dp))
            )
        }
    }
}

@Composable
fun AgendaSection(
    bookings: List<Booking>,
    onAccept: (String) -> Unit,
    onReject: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(Res.string.owner_agenda_today),
                    color = AppTheme.colors.textPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = stringResource(Res.string.owner_block_day),
                color = AppTheme.colors.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (bookings.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.owner_no_bookings),
                    color = AppTheme.colors.textSecondary
                )
            }
        } else {
            bookings.forEach { booking ->
                AgendaItem(
                    booking = booking,
                    onAccept = onAccept,
                    onReject = onReject
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun AgendaItem(
    booking: Booking,
    onAccept: (String) -> Unit,
    onReject: (String) -> Unit
) {
    val isPending = booking.status == "PENDING"
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val time = DateTimeUtils.formatTime(booking.timestamp)
                val parts = time.split(" ")
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(parts[0], color = AppTheme.colors.primary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    if (parts.size > 1) Text(parts[1], color = AppTheme.colors.primary.copy(alpha = 0.7f), fontSize = 10.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(booking.serviceName, color = AppTheme.colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(
                        text = stringResource(Res.string.owner_agenda_subtitle_format, booking.clientName, booking.durationMinutes),
                        color = AppTheme.colors.textSecondary,
                        fontSize = 12.sp
                    )
                }
                Icon(
                    imageVector = if (booking.status == "CONFIRMED") Icons.Default.CheckCircle else Icons.Default.Notifications,
                    contentDescription = null,
                    tint = if (booking.status == "CONFIRMED") AppTheme.colors.primary else AppTheme.colors.textSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            if (isPending) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { onReject(booking.id) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red.copy(alpha = 0.5f))
                    ) {
                        Text("Rechazar", fontSize = 12.sp)
                    }
                    Button(
                        onClick = { onAccept(booking.id) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary)
                    ) {
                        Text("Aceptar", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    onNavigateToProfile: () -> Unit,
    onExplorarClick: () -> Unit = {},
    onCitasClick: () -> Unit = {},
    currentScreen: String = ""
) {
    Surface(
        color = AppTheme.colors.background,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, AppTheme.colors.textPrimary.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceAround) {
            NavigationItem(
                icon = Icons.Default.DateRange,
                label = stringResource(Res.string.nav_calendar),
                isSelected = currentScreen == "Calendario" || currentScreen == "",
                onClick = onCitasClick
            )
            NavigationItem(
                icon = Icons.Default.Person,
                label = stringResource(Res.string.nav_clients),
                isSelected = currentScreen == "Explorar",
                onClick = onExplorarClick
            )
            NavigationItem(
                icon = Icons.Default.Info,
                label = stringResource(Res.string.nav_insights),
                isSelected = currentScreen == "Insights",
                onClick = {}
            )
            NavigationItem(
                icon = Icons.Default.AccountCircle,
                label = stringResource(Res.string.nav_profile),
                isSelected = currentScreen == "Perfil",
                onClick = onNavigateToProfile
            )
        }
    }
}

@Composable
private fun NavigationItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
    val color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textSecondary
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Icon(icon, label, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = color, fontSize = 10.sp)
    }
}
