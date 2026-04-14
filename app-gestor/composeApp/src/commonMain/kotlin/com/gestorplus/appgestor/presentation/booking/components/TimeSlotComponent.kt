package com.gestorplus.appgestor.presentation.booking.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.presentation.booking.theme.SelectedBlue
import com.gestorplus.appgestor.presentation.booking.theme.TextSecondary

@Composable
fun TimeSlotSection(
    title: String,
    slots: List<String>,
    selectedSlot: String?,
    onSlotSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = SelectedBlue,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.heightIn(max = 200.dp), // Adjust based on slots
            userScrollEnabled = false
        ) {
            items(slots) { slot ->
                val isSelected = slot == selectedSlot
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) SelectedBlue else MaterialTheme.colorScheme.surface.copy(alpha = 0.3f))
                        .border(
                            width = 1.dp,
                            color = if (isSelected) SelectedBlue else Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onSlotSelected(slot) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = slot,
                        color = if (isSelected) Color.White else Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
