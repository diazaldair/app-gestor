package com.gestorplus.appgestor.designsystem.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.designsystem.components.status.StatusChip

@Composable
fun AppointmentCard(
    clientName: String,
    serviceName: String,
    duration: String,
    statusText: String,
    statusColor: Color,
    modifier: Modifier = Modifier,
    onDetailsClick: () -> Unit,
    onMessageClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surface
        )
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            // Barra lateral de color indicadora de estado
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(statusColor)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusChip(text = statusText, backgroundColor = statusColor)
                    Text(
                        text = duration,
                        style = AppTheme.typography.labelLarge,
                        color = AppTheme.colors.textPrimary.copy(alpha = 0.6f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = clientName,
                    style = AppTheme.typography.headlineLarge.copy(fontSize = 18.sp),
                    color = AppTheme.colors.textPrimary
                )

                Text(
                    text = serviceName,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.textPrimary.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDetailsClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Details", style = AppTheme.typography.labelLarge)
                    }
                    OutlinedButton(
                        onClick = onMessageClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Message", style = AppTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}
