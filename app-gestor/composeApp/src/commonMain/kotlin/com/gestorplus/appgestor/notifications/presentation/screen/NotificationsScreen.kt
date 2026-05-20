package com.gestorplus.appgestor.notifications.presentation.screen

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.notifications.presentation.composable.NotificationItemComponent
import com.gestorplus.appgestor.notifications.presentation.state.NotificationsEvent
import com.gestorplus.appgestor.notifications.presentation.viewmodel.NotificationsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    viewModel: NotificationsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = AppTheme.colors.background,
        topBar = {
            NotificationsTopBar()
        },
        bottomBar = {
            NotificationsBottomBar()
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Notificaciones",
                color = AppTheme.colors.textPrimary,
                style = AppTheme.typography.headlineLarge.copy(fontSize = 24.sp),
                modifier = Modifier.padding(vertical = 20.dp)
            )

            FiltersRow(
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = { viewModel.onEvent(NotificationsEvent.OnFilterSelected(it)) }
            )

            Spacer(Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val grouped = uiState.notifications.groupBy { it.dateCategory }
                grouped.forEach { (category, notifications) ->
                    item {
                        Text(
                            text = category,
                            color = AppTheme.colors.textSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                            letterSpacing = 0.5.sp
                        )
                    }
                    items(notifications) { notification ->
                        NotificationItemComponent(
                            notification = notification,
                            onAccept = { viewModel.onEvent(NotificationsEvent.OnAcceptAppointment(notification.id)) },
                            onDecline = { viewModel.onEvent(NotificationsEvent.OnDeclineAppointment(notification.id)) }
                        )
                    }
                }
                item { Spacer(Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
fun NotificationsTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Menu, contentDescription = null, tint = AppTheme.colors.textPrimary)
            Spacer(Modifier.width(16.dp))
            Row {
                Text(
                    "SoloBook", 
                    color = AppTheme.colors.textPrimary, 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 15.sp
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    "Pro", 
                    color = AppTheme.colors.primary, 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 15.sp
                )
            }
        }
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(AppTheme.colors.surface),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = AppTheme.colors.textSecondary, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun FiltersRow(selectedFilter: String, onFilterSelected: (String) -> Unit) {
    val filters = listOf("Todas", "Citas", "Mensajes", "Sistema")
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        filters.forEach { filter ->
            val isSelected = filter == selectedFilter
            Surface(
                onClick = { onFilterSelected(filter) },
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) AppTheme.colors.primary.copy(alpha = 0.2f) else AppTheme.colors.surface,
                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, AppTheme.colors.textSecondary.copy(alpha = 0.1f))
            ) {
                Text(
                    text = filter,
                    color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textSecondary,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun NotificationsBottomBar() {
    Surface(
        color = AppTheme.colors.background,
        modifier = Modifier.fillMaxWidth(),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, AppTheme.colors.textSecondary.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem("INICIO", Icons.Default.GridView)
            BottomNavItem("CITAS", Icons.Default.CalendarToday)
            BottomNavItem("AVISOS", Icons.Default.Notifications, isSelected = true)
            BottomNavItem("PERFIL", Icons.Default.PersonOutline)
        }
    }
}

@Composable
fun BottomNavItem(label: String, icon: ImageVector, isSelected: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon, 
            contentDescription = null, 
            tint = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textSecondary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textSecondary
        )
    }
}
