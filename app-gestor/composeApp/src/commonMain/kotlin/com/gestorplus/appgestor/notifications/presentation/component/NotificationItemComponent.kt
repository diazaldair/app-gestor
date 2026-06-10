package com.gestorplus.appgestor.notifications.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.notifications.domain.model.AppNotification
import com.gestorplus.appgestor.notifications.domain.model.NotificationType

@Composable
fun NotificationItem(
    notification: AppNotification,
    onAccept: (String) -> Unit = {},
    onDecline: (String) -> Unit = {},
    onClick: (String) -> Unit = {}
) {
    val icon = when (notification.type) {
        NotificationType.APPOINTMENT_REQUEST -> Icons.Default.CalendarMonth
        NotificationType.REMINDER -> Icons.Default.NotificationsActive
        NotificationType.SYSTEM_UPDATE -> Icons.Default.Settings
    }

    val iconBgColor = when (notification.type) {
        NotificationType.APPOINTMENT_REQUEST -> Color(0xFF1E293B)
        NotificationType.REMINDER -> Color(0xFF1E293B)
        NotificationType.SYSTEM_UPDATE -> Color(0xFF1E293B)
    }

    val iconTintColor = when (notification.type) {
        NotificationType.APPOINTMENT_REQUEST -> Color(0xFF3B82F6)
        NotificationType.REMINDER -> Color(0xFF3B82F6)
        NotificationType.SYSTEM_UPDATE -> Color(0xFF3B82F6)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(notification.id) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconTintColor, modifier = Modifier.size(24.dp))
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            notification.title,
                            style = AppTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            "10m", // TODO: Format timestamp
                            style = AppTheme.typography.labelLarge.copy(
                                fontSize = 11.sp,
                                color = AppTheme.colors.textSecondary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        notification.description,
                        style = AppTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            color = AppTheme.colors.textSecondary,
                            lineHeight = 20.sp
                        )
                    )
                }
            }

            if (notification.type == NotificationType.APPOINTMENT_REQUEST) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { onAccept(notification.id) },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6).copy(alpha = 0.3f))
                    ) {
                        Text("Aceptar", color = Color(0xFF93C5FD), fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { onDecline(notification.id) },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                    ) {
                        Text("Declinar", color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}
