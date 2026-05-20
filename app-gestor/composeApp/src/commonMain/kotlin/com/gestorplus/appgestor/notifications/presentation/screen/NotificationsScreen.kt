package com.gestorplus.appgestor.notifications.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.DarkPalette
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
        containerColor = DarkPalette.background,
        topBar = {
            NotificationsTopBar()
        },
        bottomBar = {
            // Placeholder for Bottom Navigation
            NotificationsBottomBar()
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Notificaciones",
                color = DarkPalette.textPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            FiltersRow(
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = { viewModel.onEvent(NotificationsEvent.OnFilterSelected(it)) }
            )

            Spacer(Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                val grouped = uiState.notifications.groupBy { it.dateCategory }
                grouped.forEach { (category, notifications) ->
                    item {
                        Text(
                            text = category,
                            color = DarkPalette.textSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
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
            }
        }
    }
}

@Composable
fun NotificationsTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(16.dp))
            Column {
                Text("SoloBook", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Pro", color = DarkPalette.primary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )
    }
}

@Composable
fun FiltersRow(selectedFilter: String, onFilterSelected: (String) -> Unit) {
    val filters = listOf("Todas", "Citas", "Mensajes", "Sistema")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        filters.forEach { filter ->
            val isSelected = filter == selectedFilter
            Surface(
                onClick = { onFilterSelected(filter) },
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) DarkPalette.primary.copy(alpha = 0.2f) else Color.Transparent,
                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2D3748))
            ) {
                Text(
                    text = filter,
                    color = if (isSelected) DarkPalette.primary else DarkPalette.textSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun NotificationsBottomBar() {
    // Implementación simplificada del BottomBar de la imagen
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkPalette.background)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomNavItem("INICIO", "🏠")
        BottomNavItem("CITAS", "📅")
        BottomNavItem("AVISOS", "🔔", isSelected = true)
        BottomNavItem("PERFIL", "👤")
    }
}

@Composable
fun BottomNavItem(label: String, icon: String, isSelected: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 20.sp)
        Text(
            label,
            fontSize = 10.sp,
            color = if (isSelected) DarkPalette.primary else DarkPalette.textSecondary
        )
    }
}
