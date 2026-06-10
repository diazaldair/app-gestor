package com.gestorplus.appgestor.booking.presentation.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.booking.presentation.viewmodel.BookingViewModel
import com.gestorplus.appgestor.booking.presentation.state.*
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlinx.datetime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    clinicId: String,
    serviceId: String,
    onBack: () -> Unit,
    onConfirm: () -> Unit,
    viewModel: BookingViewModel = koinViewModel { parametersOf(clinicId, serviceId) }
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is BookingEfffect.NavigateBack -> onBack()
                is BookingEfffect.BookingConfirmed -> onConfirm()
                is BookingEfffect.ShowError -> {
                    // Implementación de mensaje de error si es necesario
                }
            }
        }
    }

    DsTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                state.serviceName,
                                style = AppTheme.typography.headlineLarge.copy(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.colors.textPrimary
                                )
                            )
                            Text(
                                state.clinicName,
                                style = AppTheme.typography.labelLarge.copy(
                                    fontSize = 12.sp,
                                    color = AppTheme.colors.textSecondary
                                )
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.onEvent(BookingEvent.OnBackClicked) }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = AppTheme.colors.textPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = AppTheme.colors.background
                    )
                )
            },
            bottomBar = {
                BookingFooter(
                    selectedDate = "${state.selectedMonth.take(3)} ${state.selectedDate}",
                    selectedTime = state.selectedTimeSlot,
                    onConfirm = { viewModel.onEvent(BookingEvent.OnConfirmBooking) },
                    isLoading = state.isLoading
                )
            },
            containerColor = AppTheme.colors.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                // Progress Indicator
                BookingProgressIndicator(step = 2)

                Spacer(modifier = Modifier.height(24.dp))

                // Calendar Section
                CalendarCard(
                    selectedDate = state.selectedDate,
                    month = state.selectedMonth,
                    onDateSelected = { viewModel.onEvent(BookingEvent.OnDateSelected(it)) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Available Time Slots Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Available Time Slots",
                            style = AppTheme.typography.headlineLarge.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.textPrimary
                            )
                        )
                        Text(
                            "${state.selectedDayOfWeek}, ${state.selectedMonth.substringBefore(" ")} ${state.selectedDate}",
                            style = AppTheme.typography.labelLarge.copy(
                                color = AppTheme.colors.textSecondary
                            )
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(AppTheme.colors.primary.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = AppTheme.colors.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "EST",
                                color = AppTheme.colors.primary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Time Slots Grid
                TimeSlotGroup(
                    title = "MORNING",
                    slots = state.timeSlotsMorning,
                    selectedSlot = state.selectedTimeSlot,
                    onSlotSelected = { viewModel.onEvent(BookingEvent.OnTimeSlotSelected(it)) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                TimeSlotGroup(
                    title = "AFTERNOON",
                    slots = state.timeSlotsAfternoon,
                    selectedSlot = state.selectedTimeSlot,
                    onSlotSelected = { viewModel.onEvent(BookingEvent.OnTimeSlotSelected(it)) }
                )

                Spacer(modifier = Modifier.height(120.dp)) // Espacio para el footer
            }
        }
    }
}

@Composable
fun BookingProgressIndicator(step: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(4) { index ->
            val isActive = index == step - 1
            Box(
                modifier = Modifier
                    .width(if (isActive) 48.dp else 32.dp)
                    .height(6.dp)
                    .padding(horizontal = 4.dp)
                    .clip(CircleShape)
                    .background(if (isActive) AppTheme.colors.primary else Color.White.copy(alpha = 0.2f))
            )
        }
    }
}

@Composable
fun CalendarCard(
    selectedDate: Int,
    month: String,
    onDateSelected: (Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.colors.surface,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Anterior mes */ }) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = AppTheme.colors.primary)
                }
                Text(
                    month,
                    style = AppTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.textPrimary
                    )
                )
                IconButton(onClick = { /* Siguiente mes */ }) {
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AppTheme.colors.primary)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            val days = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
            Row(modifier = Modifier.fillMaxWidth()) {
                days.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.textSecondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Rejilla de calendario dinámica básica
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val firstDayOfMonth = LocalDate(today.year, today.month, 1)
            val dayOfWeekOffset = (firstDayOfMonth.dayOfWeek.isoDayNumber % 7) // 0 for Sunday
            
            val daysInMonth = when (today.month) {
                Month.FEBRUARY -> if ((today.year % 4 == 0 && today.year % 100 != 0) || (today.year % 400 == 0)) 29 else 28
                Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
                else -> 31
            }

            val calendarDays = mutableListOf<Int?>()
            repeat(dayOfWeekOffset) { calendarDays.add(null) }
            for (i in 1..daysInMonth) { calendarDays.add(i) }
            while (calendarDays.size % 7 != 0) { calendarDays.add(null) }

            val chunkedDays = calendarDays.chunked(7)
            
            chunkedDays.forEach { week ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    week.forEach { day ->
                        val isSelected = day == selectedDate
                        
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) AppTheme.colors.primary else Color.Transparent)
                                .clickable(enabled = day != null) { day?.let { onDateSelected(it) } },
                            contentAlignment = Alignment.Center
                        ) {
                            if (day != null) {
                                Text(
                                    text = day.toString(),
                                    style = AppTheme.typography.labelLarge.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                        color = if (isSelected) Color.White else AppTheme.colors.textPrimary
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimeSlotGroup(
    title: String,
    slots: List<String>,
    selectedSlot: String?,
    onSlotSelected: (String) -> Unit
) {
    Column {
        Text(
            title,
            style = AppTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = AppTheme.colors.primary,
                letterSpacing = 2.sp
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        // Rejilla personalizada para slots de tiempo
        val rows = slots.chunked(3)
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { slot ->
                    val isSelected = slot == selectedSlot
                    val isUnavailable = false // En producción esto vendría del estado
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                when {
                                    isSelected -> AppTheme.colors.primary
                                    isUnavailable -> AppTheme.colors.surface.copy(alpha = 0.1f)
                                    else -> AppTheme.colors.background
                                }
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) AppTheme.colors.primary else Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable(enabled = !isUnavailable) { onSlotSelected(slot) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = slot,
                            style = AppTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    isSelected -> Color.White
                                    isUnavailable -> AppTheme.colors.textSecondary.copy(alpha = 0.3f)
                                    else -> AppTheme.colors.textPrimary
                                }
                            )
                        )
                    }
                }
                // Rellenar espacios vacíos en la última fila
                if (row.size < 3) {
                    repeat(3 - row.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun BookingFooter(
    selectedDate: String,
    selectedTime: String?,
    onConfirm: () -> Unit,
    isLoading: Boolean
) {
    Surface(
        color = AppTheme.colors.background.copy(alpha = 0.95f),
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 8.dp,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column {
            Row(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Selected Slot",
                        style = AppTheme.typography.labelLarge.copy(color = AppTheme.colors.textSecondary)
                    )
                    Text(
                        if (selectedTime != null) "$selectedDate, $selectedTime" else "No slot selected",
                        style = AppTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.textPrimary
                        )
                    )
                }
                
                Button(
                    onClick = onConfirm,
                    enabled = selectedTime != null && !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.height(56.dp).weight(1f).padding(start = 24.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Confirm Booking", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
