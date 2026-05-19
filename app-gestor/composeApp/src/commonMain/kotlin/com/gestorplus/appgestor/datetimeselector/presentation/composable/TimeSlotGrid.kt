package com.gestorplus.appgestor.datetimeselector.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.datetimeselector.domain.model.TimeSlot
import com.gestorplus.appgestor.designsystem.theme.AppTheme

@Composable
fun TimeSlotGrid(
    title: String,
    slots: List<TimeSlot>,
    selectedSlotId: String?,
    onSlotSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = AppTheme.colors.primary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.heightIn(max = 200.dp),
            userScrollEnabled = false
        ) {
            items(slots) { slot ->
                val isSelected = slot.id == selectedSlotId
                val isAvailable = slot.isAvailable
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when {
                                isSelected -> AppTheme.colors.primary
                                !isAvailable -> AppTheme.colors.surface.copy(alpha = 0.1f)
                                else -> AppTheme.colors.surface.copy(alpha = 0.3f)
                            }
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) AppTheme.colors.primary else Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable(enabled = isAvailable) { onSlotSelected(slot.id) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = slot.time,
                        color = when {
                            isSelected -> AppTheme.colors.onPrimary
                            !isAvailable -> AppTheme.colors.textSecondary.copy(alpha = 0.4f)
                            else -> AppTheme.colors.textPrimary
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
