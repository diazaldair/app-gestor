package com.gestorplus.appgestor.owner.working_hours.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.owner.schedule.domain.model.AutoLunchConfig
import com.gestorplus.appgestor.owner.schedule.domain.model.MasterSchedule
import com.gestorplus.appgestor.owner.schedule.domain.model.TimingDefaults
import com.gestorplus.appgestor.owner.schedule.domain.model.WorkingShift
import com.gestorplus.appgestor.owner.working_hours.presentation.state.WorkingHoursEffect
import com.gestorplus.appgestor.owner.working_hours.presentation.state.WorkingHoursEvent
import com.gestorplus.appgestor.owner.working_hours.presentation.viewmodel.WorkingHoursViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import app_gestor.composeapp.generated.resources.*

private val DarkBackground = Color(0xFF0F172A)
private val SurfaceColor = Color(0xFF1E293B)
private val PrimaryBlue = Color(0xFF3B82F6)
private val CardBorderColor = Color(0xFF334155)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkingHoursScreen(
    onBack: () -> Unit,
    onNavigateToGroupDetail: () -> Unit,
    viewModel: WorkingHoursViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is WorkingHoursEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
                WorkingHoursEffect.NavigateBack -> onBack()
                is WorkingHoursEffect.NavigateToExceptions -> onNavigateToGroupDetail() // Aquí podrías pasar la fecha
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(Res.string.working_hours_title), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(WorkingHoursEvent.NavigateBack) }) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(WorkingHoursEvent.SaveAll) }) {
                        if (state.isSaving) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        else Icon(Icons.Default.Save, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Master Schedule Section (conectado al ViewModel)
            MasterScheduleSection(
                master = state.masterSchedule,
                onMasterChanged = { viewModel.onEvent(WorkingHoursEvent.UpdateMasterSchedule(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Auto Lunch Section
            AutoLunchSection(
                config = state.autoLunch,
                onConfigChanged = { viewModel.onEvent(WorkingHoursEvent.UpdateAutoLunch(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Current Schedule Header + Reset
            CurrentScheduleHeader(onReset = { viewModel.onEvent(WorkingHoursEvent.ResetAll) })

            Spacer(modifier = Modifier.height(16.dp))

            // Group Schedule Item (Mon-Wed-Fri) - Podría ser un atajo para editar varios días
            GroupScheduleItem(
                days = listOf("M", "W", "F"),
                label = "Mon — Fri (Avg)",
                status = "FOLLOWING MASTER",
                onClick = { /* TODO: navegar a edición grupal */ }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Días individuales (se muestran los turnos por día)
            listOf(7,1,2,3,4,5,6).forEach { day -> // Domingo = 7, Lunes=1...
                val shift = state.shifts[day] ?: WorkingShift(dayOfWeek = day)
                DayScheduleItem(
                    day = day,
                    shift = shift,
                    isEnabled = shift.morningStart != null || shift.afternoonStart != null,
                    onShiftChanged = { newShift ->
                        viewModel.onEvent(WorkingHoursEvent.UpdateShift(newShift))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Timing Defaults Section
            TimingDefaultsSection(
                defaults = state.timingDefaults,
                onDefaultsChanged = { viewModel.onEvent(WorkingHoursEvent.UpdateTimingDefaults(it)) }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun MasterScheduleSection(master: MasterSchedule, onMasterChanged: (MasterSchedule) -> Unit) {
    Surface(
        color = SurfaceColor.copy(alpha = 0.5f),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Master Schedule", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Surface(color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                    Text("DEFAULT", color = Color.Gray, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TimeInputBox(
                    label = "START",
                    time = master.startTime,
                    onTimeChange = { onMasterChanged(master.copy(startTime = it)) },
                    modifier = Modifier.weight(1f)
                )
                TimeInputBox(
                    label = "END",
                    time = master.endTime,
                    onTimeChange = { onMasterChanged(master.copy(endTime = it)) },
                    modifier = Modifier.weight(1f)
                )
                Box(modifier = Modifier.size(48.dp).background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.ContentCopy, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Apply this rule to:", color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEachIndexed { index, day ->
                    val dayNum = index + 1
                    val isSelected = master.enabledDays.contains(dayNum)
                    Surface(
                        color = if (isSelected) PrimaryBlue.copy(alpha = 0.2f) else Color.Transparent,
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) PrimaryBlue else Color.Gray.copy(alpha = 0.3f)),
                        modifier = Modifier.weight(1f).clickable {
                            val newDays = if (isSelected) master.enabledDays - dayNum else master.enabledDays + dayNum
                            onMasterChanged(master.copy(enabledDays = newDays.sorted()))
                        }
                    ) {
                        Text(
                            text = day,
                            color = if (isSelected) PrimaryBlue else Color.Gray,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TimeInputBox(label: String, time: String, onTimeChange: (String) -> Unit, modifier: Modifier = Modifier) {
    var showPicker by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Surface(
            color = Color.Gray.copy(alpha = 0.1f),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderColor),
            modifier = Modifier.fillMaxWidth().clickable { showPicker = true }
        ) {
            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(label, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(time, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
    // Aquí podrías integrar un TimePicker (ej. con una alerta)
}

@Composable
fun AutoLunchSection(config: AutoLunchConfig, onConfigChanged: (AutoLunchConfig) -> Unit) {
    Surface(
        color = SurfaceColor.copy(alpha = 0.5f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Restaurant, null, tint = Color(0xFFF59E0B))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Auto Lunch", color = Color.White, fontWeight = FontWeight.Bold)
                Text("${config.startTime} - ${config.endTime} (${if (config.enabled) "Enabled" else "Disabled"})", color = Color.Gray, fontSize = 12.sp)
            }
            Switch(
                checked = config.enabled,
                onCheckedChange = { onConfigChanged(config.copy(enabled = it)) },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryBlue)
            )
            IconButton(onClick = { /* Abrir diálogo para editar horas */ }) {
                Icon(Icons.Default.Edit, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun CurrentScheduleHeader(onReset: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CalendarViewMonth, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Current Schedule", color = Color.White, fontWeight = FontWeight.Bold)
        }
        TextButton(onClick = onReset) {
            Text("RESET ALL", color = PrimaryBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun GroupScheduleItem(days: List<String>, label: String, status: String, onClick: () -> Unit) {
    Surface(
        color = SurfaceColor.copy(alpha = 0.3f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
                days.forEach { day ->
                    Box(
                        modifier = Modifier.size(32.dp).background(PrimaryBlue, CircleShape).border(2.dp, SurfaceColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(day, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(status, color = PrimaryBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.Gray)
        }
    }
}

@Composable
fun DayScheduleItem(day: Int, shift: WorkingShift, isEnabled: Boolean, onShiftChanged: (WorkingShift) -> Unit) {
    val dayName = when (day) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        6 -> "Saturday"
        else -> "Sunday"
    }
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).background(if (isEnabled) PrimaryBlue else Color.Gray.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(dayName.take(1), color = if (isEnabled) Color.White else Color.Gray, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(dayName, color = Color.White, fontWeight = FontWeight.Bold)
                    // Mostrar resumen de horarios
                    val morningStr = shift.morningStart?.let { "$it - ${shift.morningEnd}" } ?: "Closed"
                    val afternoonStr = shift.afternoonStart?.let { "$it - ${shift.afternoonEnd}" } ?: ""
                    Text("Morning: $morningStr", color = Color.Gray, fontSize = 10.sp)
                    if (afternoonStr.isNotBlank()) Text("Afternoon: $afternoonStr", color = Color.Gray, fontSize = 10.sp)
                }
                Switch(
                    checked = isEnabled,
                    onCheckedChange = { enabled ->
                        if (enabled) {
                            // Activar con horarios por defecto
                            onShiftChanged(shift.copy(morningStart = "09:00 AM", morningEnd = "01:00 PM", afternoonStart = "03:00 PM", afternoonEnd = "07:00 PM"))
                        } else {
                            // Desactivar (poner null)
                            onShiftChanged(shift.copy(morningStart = null, morningEnd = null, afternoonStart = null, afternoonEnd = null))
                        }
                    },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryBlue)
                )
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, null, tint = Color.Gray)
                }
            }

            if (isExpanded && isEnabled) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = SurfaceColor.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(start = 56.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Morning Shift", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TimeInputCompact(
                                label = "Start",
                                value = shift.morningStart ?: "",
                                onValueChange = { onShiftChanged(shift.copy(morningStart = it)) },
                                modifier = Modifier.weight(1f)
                            )
                            TimeInputCompact(
                                label = "End",
                                value = shift.morningEnd ?: "",
                                onValueChange = { onShiftChanged(shift.copy(morningEnd = it)) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Afternoon Shift", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TimeInputCompact(
                                label = "Start",
                                value = shift.afternoonStart ?: "",
                                onValueChange = { onShiftChanged(shift.copy(afternoonStart = it)) },
                                modifier = Modifier.weight(1f)
                            )
                            TimeInputCompact(
                                label = "End",
                                value = shift.afternoonEnd ?: "",
                                onValueChange = { onShiftChanged(shift.copy(afternoonEnd = it)) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimeInputCompact(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray) },
        placeholder = { Text("09:00 AM", color = Color.Gray) },
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryBlue,
            unfocusedBorderColor = Color.Gray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color(0xFF0F172A),
            unfocusedContainerColor = Color(0xFF0F172A)
        )
    )
}

@Composable
fun TimingDefaultsSection(defaults: TimingDefaults, onDefaultsChanged: (TimingDefaults) -> Unit) {
    Surface(
        color = SurfaceColor.copy(alpha = 0.5f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Timer, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Timing Defaults", color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.Info, null, tint = PrimaryBlue, modifier = Modifier.size(16.dp))
            }
            Text("These are your global presets applied to all new appointments.", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TimingInput(
                    label = "DURATION",
                    value = defaults.defaultDurationMinutes.toString(),
                    unit = "MIN",
                    onValueChange = { value ->
                        val int = value.toIntOrNull() ?: defaults.defaultDurationMinutes
                        onDefaultsChanged(defaults.copy(defaultDurationMinutes = int))
                    },
                    modifier = Modifier.weight(1f)
                )
                TimingInput(
                    label = "BUFFER",
                    value = defaults.defaultBufferMinutes.toString(),
                    unit = "MIN",
                    onValueChange = { value ->
                        val int = value.toIntOrNull() ?: defaults.defaultBufferMinutes
                        onDefaultsChanged(defaults.copy(defaultBufferMinutes = int))
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun TimingInput(label: String, value: String, unit: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = { Text(unit, color = Color.Gray) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = CardBorderColor,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = SurfaceColor,
                unfocusedContainerColor = SurfaceColor
            )
        )
    }
}