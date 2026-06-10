package com.gestorplus.appgestor.notifications.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import app_gestor.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import com.gestorplus.appgestor.notifications.presentation.viewmodel.NotificationsViewModel
import com.gestorplus.appgestor.notifications.presentation.component.NotificationItem
import com.gestorplus.appgestor.notifications.presentation.component.NotificationFilterChips
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.owner.dashboard.presentation.screen.BottomNavigationBar
import com.gestorplus.appgestor.notifications.presentation.state.NotificationsEvent
import com.gestorplus.appgestor.notifications.presentation.state.NotificationsEfffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
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

    Scaffold(
        topBar = {
                TopAppBar(
                    title = { Text(stringResource(Res.string.notifications_title), color = AppTheme.colors.textPrimary) },
                    navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null, tint = AppTheme.colors.textPrimary) } },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = AppTheme.colors.background)
                )
        },
        containerColor = AppTheme.colors.background,
        bottomBar = { BottomNavigationBar(onNavigateToProfile = {}) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            NotificationFilterChips(current = state.filter) { filter -> viewModel.onEvent(NotificationsEvent.ChangeFilter(filter)) }

            Spacer(modifier = Modifier.height(16.dp))

            if (!state.isLoading && state.notifications.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text(stringResource(Res.string.notifications_empty), color = AppTheme.colors.textSecondary)
                }
            } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                val today = state.notifications.filter { it.dateCategory == "TODAY" }
                val yesterday = state.notifications.filter { it.dateCategory == "YESTERDAY" }

                if (today.isNotEmpty()) {
                    item {
                        Text(
                            stringResource(Res.string.notifications_section_today),
                            color = AppTheme.colors.textSecondary,
                            style = AppTheme.typography.bodyMedium.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(today) { n ->
                        NotificationItem(
                            notification = n,
                            onAccept = { id -> viewModel.onEvent(NotificationsEvent.Accept(id)) },
                            onDecline = { id, reason -> viewModel.onEvent(NotificationsEvent.Decline(id, reason)) },
                            onClick = { id -> viewModel.onEvent(NotificationsEvent.MarkRead(id)) }
                        )
                    }
                }

                if (yesterday.isNotEmpty()) {
                    item {
                        Text(
                            stringResource(Res.string.notifications_section_yesterday),
                            color = AppTheme.colors.textSecondary,
                            style = AppTheme.typography.bodyMedium.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(yesterday) { n ->
                        NotificationItem(
                            notification = n,
                            onAccept = { id -> viewModel.onEvent(NotificationsEvent.Accept(id)) },
                            onDecline = { id, reason -> viewModel.onEvent(NotificationsEvent.Decline(id, reason)) },
                            onClick = { id -> viewModel.onEvent(NotificationsEvent.MarkRead(id)) }
                        )
                    }
                }
            }
            }
        }
    }
}
