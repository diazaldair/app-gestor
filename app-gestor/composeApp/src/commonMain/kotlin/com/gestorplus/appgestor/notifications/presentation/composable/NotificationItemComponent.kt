package com.gestorplus.appgestor.notifications.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.gestorplus.appgestor.designsystem.theme.DarkPalette

@Composable
fun NotificationItemComponent(
    notification: Notification,
    onAccept: () -> Unit = {},
    onDecline: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = DarkPalette.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkPalette.primary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    val icon = when(notification.type) {
                        NotificationType.APPOINTMENT_REQUEST -> "📅"
                        NotificationType.REMINDER -> "⏰"
                        NotificationType.SYSTEM_UPDATE -> "⚙️"
                    }
                    Text(icon, fontSize = 20.sp)
                }

                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            notification.title,
                            color = DarkPalette.textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            notification.time,
                            color = DarkPalette.textSecondary,
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        notification.description,
                        color = DarkPalette.textSecondary,
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
                        colors = ButtonDefaults.buttonColors(containerColor = DarkPalette.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Aceptar")
                    }
                    Spacer(Modifier.width(12.dp))
                    Button(
                        onClick = onDecline,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D3748)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Declinar", color = Color.White)
                    }
                }
            }
        }
    }
}
