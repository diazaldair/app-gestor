package com.gestorplus.appgestor.presentation.booking.components

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
import androidx.compose.material3.MaterialTheme
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
import com.gestorplus.appgestor.presentation.booking.theme.SelectedBlue
import com.gestorplus.appgestor.presentation.booking.theme.TextSecondary

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
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
            .padding(16.dp)
    ) {
        // Month Selector Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Previous Month */ }) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Prev", tint = Color.White)
            }
            Text(
                text = month,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { /* Next Month */ }) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Day Labels
        val days = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
        Row(modifier = Modifier.fillMaxWidth()) {
            days.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Calendar Grid (Simulating October 2023)
        // Starts on Sunday Oct 1st
        val dates = (27..30).toList() + (1..14).toList() // Showing partial for simplicity like the image
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(180.dp), // Fixed height for simplicity
            userScrollEnabled = false
        ) {
            items(dates) { date ->
                val isCurrentMonth = date in 1..31
                val isSelected = date == selectedDate && isCurrentMonth
                
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) SelectedBlue else Color.Transparent)
                        .clickable(enabled = isCurrentMonth) { onDateSelected(date) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.toString(),
                        color = when {
                            isSelected -> Color.White
                            isCurrentMonth -> Color.White
                            else -> TextSecondary.copy(alpha = 0.5f)
                        },
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
