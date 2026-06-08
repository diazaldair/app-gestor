package com.gestorplus.appgestor.notifications.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gestorplus.appgestor.notifications.presentation.state.NotificationFilter

@Composable
fun NotificationFilterChips(
    current: NotificationFilter,
    onSelect: (NotificationFilter) -> Unit
) {
    var selected by remember { mutableStateOf(current) }
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(selected = selected == NotificationFilter.All, onClick = { selected = NotificationFilter.All; onSelect(NotificationFilter.All) }, label = { Text("Todas") })
        FilterChip(selected = selected == NotificationFilter.Appointments, onClick = { selected = NotificationFilter.Appointments; onSelect(NotificationFilter.Appointments) }, label = { Text("Citas") })
        FilterChip(selected = selected == NotificationFilter.Messages, onClick = { selected = NotificationFilter.Messages; onSelect(NotificationFilter.Messages) }, label = { Text("Mensajes") })
        FilterChip(selected = selected == NotificationFilter.System, onClick = { selected = NotificationFilter.System; onSelect(NotificationFilter.System) }, label = { Text("Sistema") })
    }
}
