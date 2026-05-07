package com.gestorplus.appgestor.presentation.booking

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
import com.gestorplus.appgestor.presentation.booking.components.CalendarComponent
import com.gestorplus.appgestor.presentation.booking.components.TimeSlotSection
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.presentation.booking.theme.SelectedBlue
import com.gestorplus.appgestor.presentation.booking.theme.TextSecondary
import org.jetbrains.compose.resources.stringResource
import app_gestor.composeapp.generated.resources.Res
import app_gestor.composeapp.generated.resources.booking_title
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookingScreen(
    viewModel: BookingViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    DsTheme {
        Scaffold(
            topBar = {
                BookingTopBar(onBack = { viewModel.onEvent(BookingEvent.OnBackClicked) })
            },
            bottomBar = {
                BookingBottomBar(
                    selectedDate = "Oct ${state.selectedDate}",
                    selectedTime = state.selectedTimeSlot,
                    onConfirm = { viewModel.onEvent(BookingEvent.OnConfirmBooking) }
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
                
                ProgressIndicator()

                Spacer(modifier = Modifier.height(24.dp))

                CalendarComponent(
                    selectedDate = state.selectedDate,
                    month = state.selectedMonth,
                    onDateSelected = { viewModel.onEvent(BookingEvent.OnDateSelected(it)) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "Available Time Slots",
                            color = AppTheme.colors.textPrimary,
                            style = AppTheme.typography.headlineLarge.copy(fontSize = 20.sp)
                        )
                        Text(
                            text = "Thursday, October 5",
                            color = AppTheme.colors.textPrimary.copy(alpha = 0.6f),
                            style = AppTheme.typography.bodyMedium.copy(fontSize = 14.sp)
                        )
                    }
                    
                    Surface(
                        color = AppTheme.colors.primary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(AppTheme.colors.primary)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("EST", color = AppTheme.colors.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TimeSlotSection(
                    title = "MORNING",
                    slots = state.timeSlotsMorning,
                    selectedSlot = state.selectedTimeSlot,
                    onSlotSelected = { viewModel.onEvent(BookingEvent.OnTimeSlotSelected(it)) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                TimeSlotSection(
                    title = "AFTERNOON",
                    slots = state.timeSlotsAfternoon,
                    selectedSlot = state.selectedTimeSlot,
                    onSlotSelected = { viewModel.onEvent(BookingEvent.OnTimeSlotSelected(it)) }
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingTopBar(onBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.booking_title),
                color = AppTheme.colors.textPrimary,
                style = AppTheme.typography.headlineLarge.copy(fontSize = 18.sp)
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AppTheme.colors.textPrimary)
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun ProgressIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(4) { index ->
            val isActive = index == 1
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .padding(horizontal = 2.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (isActive) AppTheme.colors.primary else AppTheme.colors.textPrimary.copy(alpha = 0.2f))
            )
        }
    }
}

@Composable
fun BookingBottomBar(
    selectedDate: String,
    selectedTime: String?,
    onConfirm: () -> Unit
) {
    Surface(
        color = AppTheme.colors.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Selected Slot", color = AppTheme.colors.textPrimary.copy(alpha = 0.6f), fontSize = 12.sp)
                Text(
                    "$selectedDate, ${selectedTime ?: "Select a slot"}",
                    color = AppTheme.colors.textPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Confirm Booking", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
