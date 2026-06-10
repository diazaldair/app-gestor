package com.gestorplus.appgestor.notifications.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.notifications.domain.model.AppNotification
import app_gestor.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotificationItem(
    notification: AppNotification,
    onAccept: (String) -> Unit = {},
    onDecline: (String, String?) -> Unit = { _, _ -> },
    onClick: (String) -> Unit = {}
) {
    var showDeclineDialog by remember { mutableStateOf(false) }
    var declineReason by remember { mutableStateOf("") }

    if (showDeclineDialog) {
        AlertDialog(
            onDismissRequest = { showDeclineDialog = false },
            title = { Text("Rechazar Cita", color = AppTheme.colors.textPrimary) },
            text = {
                Column {
                    Text("¿Deseas agregar un motivo para el paciente?", color = AppTheme.colors.textSecondary)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = declineReason,
                        onValueChange = { declineReason = it },
                        placeholder = { Text("Ej: Horario no disponible") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = AppTheme.colors.textPrimary,
                            unfocusedTextColor = AppTheme.colors.textPrimary
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onDecline(notification.id, declineReason.ifBlank { null })
                    showDeclineDialog = false
                }) {
                    Text("Confirmar Rechazo", color = AppTheme.colors.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeclineDialog = false }) {
                    Text("Cancelar")
                }
            },
            containerColor = AppTheme.colors.surface
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick(notification.id) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(notification.title, style = AppTheme.typography.bodyMedium.copy(fontSize = 16.sp, fontWeight = FontWeight.SemiBold), color = AppTheme.colors.textPrimary)
                Spacer(modifier = Modifier.weight(1f))
                if (!notification.isRead) {
                    Box(modifier = Modifier.size(8.dp).background(AppTheme.colors.primary, RoundedCornerShape(4.dp)))
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(notification.description, style = AppTheme.typography.bodyMedium.copy(fontSize = 14.sp), color = AppTheme.colors.textSecondary)

            if (notification.type.name == "APPOINTMENT_REQUEST" && notification.appointmentStatus == null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Button(
                        onClick = { onAccept(notification.id) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary)
                    ) {
                        Text(stringResource(Res.string.notifications_btn_accept), color = AppTheme.colors.onPrimary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = { showDeclineDialog = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AppTheme.colors.textPrimary)
                    ) {
                        Text(stringResource(Res.string.notifications_btn_decline), color = AppTheme.colors.textPrimary)
                    }
                }
            } else if (notification.appointmentStatus != null) {
                Spacer(modifier = Modifier.height(8.dp))
                val statusText = if (notification.appointmentStatus == "ACCEPTED") "ACEPTADA" else "RECHAZADA"
                val statusColor = if (notification.appointmentStatus == "ACCEPTED") Color(0xFF10B981) else Color(0xFFEF4444)
                
                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.3f))
                ) {
                    Text(
                        statusText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = AppTheme.typography.labelLarge.copy(
                            fontSize = 10.sp,
                            color = statusColor,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
