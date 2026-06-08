package com.gestorplus.appgestor.notifications.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app_gestor.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import com.gestorplus.appgestor.notifications.presentation.viewmodel.NotificationsViewModel
import com.gestorplus.appgestor.notifications.presentation.component.NotificationItem
import com.gestorplus.appgestor.notifications.presentation.component.NotificationFilterChips

@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    viewModel: NotificationsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { eff ->
            when (eff) {
                is com.gestorplus.appgestor.notifications.presentation.state.NotificationsEfffect.ShowMessage -> {
                    // TODO: show snackbar
                }
            }
        }
    }

    Scaffold(topBar = {
        SmallTopAppBar(title = { Text(stringResource(Res.string.notifications_title)) }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null) }
        })
    }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            NotificationFilterChips(current = state.filter) { filter -> viewModel.onEvent(com.gestorplus.appgestor.notifications.presentation.state.NotificationsEvent.ChangeFilter(filter)) }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.notifications) { n ->
                    NotificationItem(
                        notification = n,
                        onAccept = { viewModel.onEvent(com.gestorplus.appgestor.notifications.presentation.state.NotificationsEvent.Accept(it)) },
                        onDecline = { viewModel.onEvent(com.gestorplus.appgestor.notifications.presentation.state.NotificationsEvent.Decline(it)) },
                        onClick = { viewModel.onEvent(com.gestorplus.appgestor.notifications.presentation.state.NotificationsEvent.MarkRead(it)) }
                    )
                }
            }
        }
    }
}
