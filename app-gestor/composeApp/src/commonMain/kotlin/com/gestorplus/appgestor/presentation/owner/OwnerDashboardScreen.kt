package com.gestorplus.appgestor.presentation.owner

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.data.local.entity.BookingEntity
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.resources.stringResource
import app_gestor.composeapp.generated.resources.*

@Composable
fun OwnerDashboardScreen(
    viewModel: OwnerDashboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is OwnerDashboardEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    DsTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                BottomNavigationBar()
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
                DashboardHeader()
                Spacer(modifier = Modifier.height(24.dp))
                StatusFilters()
                Spacer(modifier = Modifier.height(24.dp))
                CalendarGrid()
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
                    bookings = state.bookings,
                    onAccept = { viewModel.onEvent(OwnerDashboardEvent.OnAcceptBooking(it)) },
                    onReject = { viewModel.onEvent(OwnerDashboardEvent.OnRejectBooking(it)) }
                )
            }
        }
    }
}

@Composable
fun DashboardHeader() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                IconButton(onClick = { /* TODO: Event */ }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(Res.string.nav_calendar),
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
                    text = stringResource(Res.string.owner_date_mock),
                    color = AppTheme.colors.textPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(Icons.Default.KeyboardArrowDown, null, tint = AppTheme.colors.textSecondary)
            }
            Row {
                IconButton(
                    onClick = { /* TODO */ },
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
                    onClick = { /* TODO */ },
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
fun StatusFilters() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(stringResource(Res.string.status_confirmed), Icons.Default.CheckCircle, true, Modifier.weight(1f))
        FilterChip(stringResource(Res.string.status_pending), Icons.Default.Notifications, false, Modifier.weight(1f))
        FilterChip(stringResource(Res.string.status_blocked), Icons.Default.Lock, false, Modifier.weight(1f))
    }
}

@Composable
fun FilterChip(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, modifier: Modifier) {
    Surface(
        modifier = modifier.height(40.dp),
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
fun CalendarGrid() {
    val daysOfWeek = listOf(
        Res.string.day_sun,
        Res.string.day_mon,
        Res.string.day_tue,
        Res.string.day_wed,
        Res.string.day_thu,
        Res.string.day_fri,
        Res.string.day_sat
    )
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
                items(31) { index ->
                    val day = index + 1
                    CalendarDayItem(day, day == 5, day % 7 == 3)
                }
            }
        }
    }
}

@Composable
fun CalendarDayItem(day: Int, isSelected: Boolean, hasAppointments: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(vertical = 4.dp)) {
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
    bookings: List<BookingEntity>,
    onAccept: (String) -> Unit,
    onReject: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.owner_agenda_today),
                color = AppTheme.colors.textPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(Res.string.owner_block_day),
                color = AppTheme.colors.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (bookings.isEmpty()) {
            Text(
                text = stringResource(Res.string.owner_no_bookings),
                color = AppTheme.colors.textSecondary,
                modifier = Modifier.padding(24.dp)
            )
        } else {
            bookings.forEach { booking ->
                AgendaItem(
                    time = "10:00 AM",
                    title = booking.serviceName,
                    subtitle = stringResource(Res.string.owner_agenda_subtitle_format, booking.clientName, booking.durationMinutes),
                    statusIcon = if (booking.status == "CONFIRMED") Icons.Default.CheckCircle else Icons.Default.Notifications,
                    statusColor = if (booking.status == "CONFIRMED") AppTheme.colors.primary else AppTheme.colors.textSecondary,
                    onClick = {
                        if (booking.status != "CONFIRMED") onAccept(booking.id)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Ítem de Personal Time
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Transparent,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.colors.textSecondary.copy(alpha = 0.3f))
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(40.dp).background(AppTheme.colors.textSecondary.copy(alpha = 0.2f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Lock, null, tint = AppTheme.colors.textSecondary)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(Res.string.owner_personal_time),
                        color = AppTheme.colors.textPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = stringResource(Res.string.owner_personal_time_desc),
                        color = AppTheme.colors.textSecondary,
                        fontSize = 12.sp
                    )
                }
                Icon(Icons.Default.MoreVert, null, tint = AppTheme.colors.textSecondary)
            }
        }
    }
}

@Composable
fun AgendaItem(
    time: String,
    title: String,
    subtitle: String,
    statusIcon: androidx.compose.ui.graphics.vector.ImageVector,
    statusColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            val parts = time.split(" ")
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(parts[0], color = AppTheme.colors.primary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                if (parts.size > 1) Text(parts[1], color = AppTheme.colors.primary.copy(alpha = 0.7f), fontSize = 10.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = AppTheme.colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(subtitle, color = AppTheme.colors.textSecondary, fontSize = 12.sp)
            }
            Icon(statusIcon, null, tint = statusColor, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
fun BottomNavigationBar() {
    Surface(
        color = AppTheme.colors.background,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, AppTheme.colors.textPrimary.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceAround) {
            NavigationItem(Icons.Default.DateRange, stringResource(Res.string.nav_calendar), true)
            NavigationItem(Icons.Default.Person, stringResource(Res.string.nav_clients), false)
            NavigationItem(Icons.Default.Info, stringResource(Res.string.nav_insights), false)
            NavigationItem(Icons.Default.AccountCircle, stringResource(Res.string.nav_profile), false)
        }
    }
}

@Composable
fun NavigationItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean) {
    val color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textSecondary
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { /* TODO */ }) {
        Icon(icon, label, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = color, fontSize = 10.sp)
    }
}
