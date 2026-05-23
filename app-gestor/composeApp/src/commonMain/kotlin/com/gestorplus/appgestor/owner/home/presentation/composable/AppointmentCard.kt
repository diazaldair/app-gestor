package com.gestorplus.appgestor.owner.home.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.owner.home.domain.model.Appointment
import com.gestorplus.appgestor.owner.home.domain.model.AppointmentStatus

@Composable
fun NextAppointmentCard(
    appointment: Appointment,
    onStartConsultation: () -> Unit,
    onChatClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val CardBg = Color(0xFF1A2332)
    val BrandBlue = Color(0xFF3B82F6)
    val AccentGreen = Color(0xFF22C55E)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Client info row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(BrandBlue.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = appointment.clientName.first().toString(),
                        color = BrandBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = appointment.clientName,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = appointment.serviceType,
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 13.sp
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = appointment.timeLabel,
                        color = AccentGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    if (appointment.countdownLabel.isNotEmpty()) {
                        Text(
                            text = appointment.countdownLabel,
                            color = Color.White.copy(alpha = 0.4f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onStartConsultation,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandBlue.copy(alpha = 0.15f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).height(44.dp)
                ) {
                    Text(
                        text = "Iniciar Consulta",
                        color = BrandBlue,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF2A3A4A))
                        .clickable { onChatClicked() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Chat",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AppointmentTimelineItem(
    appointment: Appointment,
    isLast: Boolean = false,
    modifier: Modifier = Modifier
) {
    val GrayDot = Color(0xFF4A5568)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Timeline indicator
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        if (appointment.status == AppointmentStatus.FREE_SLOT)
                            GrayDot.copy(alpha = 0.4f)
                        else GrayDot
                    )
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(40.dp)
                        .background(GrayDot.copy(alpha = 0.3f))
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = appointment.clientName,
                color = if (appointment.status == AppointmentStatus.FREE_SLOT)
                    Color.White.copy(alpha = 0.4f) else Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Text(
                text = appointment.serviceType,
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 12.sp
            )
        }

        Text(
            text = appointment.timeLabel,
            color = Color.White.copy(alpha = 0.6f),
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp
        )
    }
}
