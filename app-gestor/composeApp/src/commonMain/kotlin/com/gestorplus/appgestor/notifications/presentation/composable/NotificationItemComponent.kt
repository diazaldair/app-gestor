package com.gestorplus.appgestor.notifications.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.notifications.domain.model.Notification
import com.gestorplus.appgestor.notifications.domain.model.NotificationType
import com.gestorplus.appgestor.designsystem.theme.AppTheme

@Composable
fun NotificationItemComponent(
    notification: Notification,
    onAccept: () -> Unit = {},
    onDecline: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AppTheme.colors.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    val icon = when(notification.type) {
                        NotificationType.APPOINTMENT_REQUEST -> Icons.Default.CalendarMonth
                        NotificationType.REMINDER -> Icons.Default.Notifications
                        NotificationType.SYSTEM_UPDATE -> Icons.Default.Settings
                    }
                    Icon(
                        imageVector = icon, 
                        contentDescription = null, 
                        tint = AppTheme.colors.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            notification.title,
                            color = AppTheme.colors.textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            notification.time,
                            color = AppTheme.colors.textSecondary,
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        notification.description,
                        color = AppTheme.colors.textSecondary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
            
            if (notification.type == NotificationType.APPOINTMENT_REQUEST) {
                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onAccept,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Aceptar", color = AppTheme.colors.onPrimary)
                    }
                    Spacer(Modifier.width(12.dp))
                    Button(
                        onClick = onDecline,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.colors.textSecondary.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Declinar", color = AppTheme.colors.textPrimary)
                    }
                }
            }
        }
    }
}
