package com.gestorplus.appgestor.datetimeselector.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.datetimeselector.presentation.composable.TimeSlotGrid
import com.gestorplus.appgestor.datetimeselector.presentation.state.DateTimeSelectorEvent
import com.gestorplus.appgestor.datetimeselector.presentation.viewmodel.DateTimeSelectorViewModel
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.booking.presentation.composable.CalendarComponent
import org.jetbrains.compose.resources.stringResource
import app_gestor.composeapp.generated.resources.*
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeSelectorScreen(
    viewModel: DateTimeSelectorViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    DsTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(Res.string.date_time_selector_title),
                            style = AppTheme.typography.headlineLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.onEvent(DateTimeSelectorEvent.OnBackClicked) }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(Res.string.common_back),
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = AppTheme.colors.background
                    )
                )
            },
            bottomBar = {
                DateTimeSelectorBottomBar(
                    selectedDate = "Oct ${state.selectedDate}",
                    selectedTime = (state.morningSlots + state.afternoonSlots).find { it.id == state.selectedTimeSlot }?.time,
                    onConfirm = { viewModel.onEvent(DateTimeSelectorEvent.OnConfirmClicked) }
                )
            },
            containerColor = AppTheme.colors.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                
                DateTimeProgressIndicator()

                Spacer(modifier = Modifier.height(24.dp))

                CalendarComponent(
                    selectedDate = state.selectedDate,
                    month = state.selectedMonth,
                    onDateSelected = { date -> 
                        viewModel.onEvent(DateTimeSelectorEvent.OnDateSelected(date)) 
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(Res.string.date_time_selector_available_slots),
                            color = Color.White,
                            style = AppTheme.typography.headlineLarge.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = stringResource(Res.string.booking_selected_date_mock),
                            color = Color(0xFF94A3B8),
                            style = AppTheme.typography.bodyMedium.copy(fontSize = 14.sp)
                        )
                    }
                    
                    Surface(
                        color = AppTheme.colors.primary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(AppTheme.colors.primary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = stringResource(Res.string.booking_timezone_est),
                                color = AppTheme.colors.primary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                TimeSlotGrid(
                    title = stringResource(Res.string.booking_morning),
                    slots = state.morningSlots,
                    selectedSlotId = state.selectedTimeSlot,
                    onSlotSelected = { slotId -> 
                        viewModel.onEvent(DateTimeSelectorEvent.OnTimeSlotSelected(slotId)) 
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                TimeSlotGrid(
                    title = stringResource(Res.string.booking_afternoon),
                    slots = state.afternoonSlots,
                    selectedSlotId = state.selectedTimeSlot,
                    onSlotSelected = { slotId -> 
                        viewModel.onEvent(DateTimeSelectorEvent.OnTimeSlotSelected(slotId)) 
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun DateTimeProgressIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(4) { index ->
            val isActive = index == 1
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(if (isActive) AppTheme.colors.primary else AppTheme.colors.surface.copy(alpha = 0.3f))
            )
        }
    }
}

@Composable
fun DateTimeSelectorBottomBar(
    selectedDate: String,
    selectedTime: String?,
    onConfirm: () -> Unit
) {
    Surface(
        color = AppTheme.colors.background,
        modifier = Modifier.fillMaxWidth(),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(Res.string.date_time_selector_selected_slot_label),
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                )
                Text(
                    text = if (selectedTime != null) "$selectedDate, $selectedTime" else stringResource(Res.string.date_time_selector_select_a_slot),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
                enabled = selectedTime != null
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(Res.string.date_time_selector_confirm_booking),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward, 
                        contentDescription = null, 
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}
