package com.gestorplus.appgestor.notifications.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.notifications.presentation.component.NotificationFilterChips
import com.gestorplus.appgestor.notifications.presentation.component.NotificationItem
import com.gestorplus.appgestor.notifications.presentation.state.NotificationsEfffect
import com.gestorplus.appgestor.notifications.presentation.state.NotificationsEvent
import com.gestorplus.appgestor.notifications.presentation.viewmodel.NotificationsViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToAppointments: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: NotificationsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { eff ->
            when (eff) {
                is NotificationsEfffect.ShowMessage -> {
                    // TODO: show snackbar
                }
            }
        }
    }

    DsTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "SoloBook",
                                style = AppTheme.typography.headlineLarge.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.colors.textPrimary
                                )
                            )
                            Text(
                                "Pro",
                                style = AppTheme.typography.headlineLarge.copy(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.colors.textPrimary
                                )
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { /* Open drawer */ }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = AppTheme.colors.textPrimary)
                        }
                    },
                    actions = {
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?q=80&w=2070&auto=format&fit=crop",
                            contentDescription = "Profile",
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = AppTheme.colors.background
                    )
                )
            },
            containerColor = AppTheme.colors.background,
            bottomBar = {
                ProBottomNavigationBar(
                    currentScreen = "AVISOS",
                    onNavigateToHome = onNavigateToHome,
                    onNavigateToAppointments = onNavigateToAppointments,
                    onNavigateToProfile = onNavigateToProfile
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    "Notificaciones",
                    style = AppTheme.typography.headlineLarge.copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.textPrimary
                    ),
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                NotificationFilterChips(current = state.filter) { filter ->
                    viewModel.onEvent(NotificationsEvent.ChangeFilter(filter))
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (!state.isLoading && state.notifications.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay notificaciones", color = AppTheme.colors.textSecondary)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val today = state.notifications.filter { it.dateCategory == "TODAY" }
                        val yesterday = state.notifications.filter { it.dateCategory == "YESTERDAY" }

                        if (today.isNotEmpty()) {
                            item {
                                Text(
                                    "HOY",
                                    style = AppTheme.typography.labelLarge.copy(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AppTheme.colors.textSecondary
                                    ),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(today) { n ->
                                NotificationItem(
                                    notification = n,
                                    onAccept = { viewModel.onEvent(NotificationsEvent.Accept(it)) },
                                    onDecline = { viewModel.onEvent(NotificationsEvent.Decline(it)) },
                                    onClick = { viewModel.onEvent(NotificationsEvent.MarkRead(it)) }
                                )
                            }
                        }

                        if (yesterday.isNotEmpty()) {
                            item {
                                Text(
                                    "AYER",
                                    style = AppTheme.typography.labelLarge.copy(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AppTheme.colors.textSecondary
                                    ),
                                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 0.dp)
                                )
                            }
                            items(yesterday) { n ->
                                NotificationItem(
                                    notification = n,
                                    onAccept = { viewModel.onEvent(NotificationsEvent.Accept(it)) },
                                    onDecline = { viewModel.onEvent(NotificationsEvent.Decline(it)) },
                                    onClick = { viewModel.onEvent(NotificationsEvent.MarkRead(it)) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProBottomNavigationBar(
    currentScreen: String,
    onNavigateToHome: () -> Unit,
    onNavigateToAppointments: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Surface(
        color = AppTheme.colors.background,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProNavItem(Icons.Default.GridView, "INICIO", currentScreen == "INICIO", onNavigateToHome)
            ProNavItem(Icons.Default.CalendarMonth, "CITAS", currentScreen == "CITAS", onNavigateToAppointments)
            ProNavItem(Icons.Default.Notifications, "AVISOS", currentScreen == "AVISOS", {})
            ProNavItem(Icons.Default.Person, "PERFIL", currentScreen == "PERFIL", onNavigateToProfile)
        }
    }
}

@Composable
fun ProNavItem(icon: ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
    val color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textSecondary
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}
