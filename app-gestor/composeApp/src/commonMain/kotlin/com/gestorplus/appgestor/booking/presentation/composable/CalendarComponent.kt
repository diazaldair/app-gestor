package com.gestorplus.appgestor.booking.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import app_gestor.composeapp.generated.resources.*

@Composable
fun CalendarComponent(
    selectedDate: Int,
    month: String,
    onDateSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E293B)) // Slate 800 - Match Image exactly
            .padding(16.dp)
    ) {
        // Month Selector Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Previous Month */ }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = stringResource(Res.string.common_prev),
                    tint = AppTheme.colors.primary
                )
            }
            Text(
                text = month,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { /* Next Month */ }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = stringResource(Res.string.common_next),
                    tint = AppTheme.colors.primary
                )
            }
        }

        // Day Labels
        val days = listOf(
            Res.string.day_sun,
            Res.string.day_mon,
            Res.string.day_tue,
            Res.string.day_wed,
            Res.string.day_thu,
            Res.string.day_fri,
            Res.string.day_sat
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
        ) {
            days.forEach { dayRes ->
                Text(
                    text = stringResource(dayRes),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = Color(0xFF94A3B8), // Slate 400 - Match Image
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Complete Calendar Grid for October 2023
        // Image 3 shows it starts with 27, 28, 29, 30 from previous month
        val daysList = listOf(27, 28, 29, 30) + (1..31).toList()
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(280.dp),
            userScrollEnabled = false,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(daysList) { day ->
                val isFromPreviousMonth = day > 20 && daysList.indexOf(day) < 4
                val isCurrentMonth = !isFromPreviousMonth
                val isSelected = day == selectedDate && isCurrentMonth
                
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) AppTheme.colors.primary else Color.Transparent)
                        .clickable(enabled = isCurrentMonth) { onDateSelected(day) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.toString(),
                        color = when {
                            isSelected -> Color.White
                            isFromPreviousMonth -> Color(0xFF475569) // Muted slate for prev month
                            else -> Color.White
                        },
                        fontSize = 15.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
