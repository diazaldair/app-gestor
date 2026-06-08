package com.gestorplus.appgestor.notifications.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.notifications.domain.model.AppNotification

@Composable
fun NotificationItem(
    notification: AppNotification,
    onAccept: (String) -> Unit = {},
    onDecline: (String) -> Unit = {},
    onClick: (String) -> Unit = {}
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp)
        .clickable { onClick(notification.id) },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(notification.title, style = AppTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.weight(1f))
                if (!notification.isRead) {
                    Box(modifier = Modifier.size(8.dp).background(AppTheme.colors.primary, RoundedCornerShape(4.dp)))
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(notification.body, style = AppTheme.typography.bodySmall, color = AppTheme.colors.textSecondary)

            if (notification.type.name == "APPOINTMENT_REQUEST") {
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Button(onClick = { onAccept(notification.id) }, modifier = Modifier.weight(1f)) {
                        Text("Aceptar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(onClick = { onDecline(notification.id) }, modifier = Modifier.weight(1f)) {
                        Text("Rechazar")
                    }
                }
            }
        }
    }
}
