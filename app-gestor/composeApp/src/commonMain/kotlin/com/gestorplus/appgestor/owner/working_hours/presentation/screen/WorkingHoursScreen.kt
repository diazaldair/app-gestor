package com.gestorplus.appgestor.owner.presentation.screen

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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.owner.setup_schedule.domain.model.Shift
import com.gestorplus.appgestor.owner.working_hours.presentation.state.WorkingHoursEfffect
import com.gestorplus.appgestor.owner.working_hours.presentation.state.WorkingHoursEvent
import com.gestorplus.appgestor.owner.working_hours.presentation.viewmodel.WorkingHoursViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkingHoursScreen(
    onBack: () -> Unit,
    viewModel: WorkingHoursViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is WorkingHoursEfffect.NavigateBack -> onBack()
                is WorkingHoursEfffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    DsTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text("Working Hours", color = AppTheme.colors.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, "Atrás", tint = AppTheme.colors.textPrimary)
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(Icons.Default.MoreVert, null, tint = AppTheme.colors.textPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = AppTheme.colors.background,
            bottomBar = {
                Box(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = { viewModel.onEvent(WorkingHoursEvent.SaveChanges) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary),
                        enabled = !state.isSaving
                    ) {
                        if (state.isSaving) {
                            CircularProgressIndicator(color = AppTheme.colors.onPrimary, modifier = Modifier.size(24.dp))
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Save, null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Save All Changes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        ) { padding ->
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppTheme.colors.primary)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        "GESTIÓN DE TURNOS", 
                        color = AppTheme.colors.textSecondary, 
                        fontSize = 12.sp, 
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AppTheme.colors.surface.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .border(1.dp, AppTheme.colors.textSecondary.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        state.shifts.forEach { shift ->
                            ShiftCard(
                                shift = shift,
                                onToggleDay = { day -> viewModel.onEvent(WorkingHoursEvent.ToggleDayInShift(shift.id, day)) },
                                onDelete = { viewModel.onEvent(WorkingHoursEvent.DeleteShift(shift.id)) }
                            )
                            if (shift != state.shifts.last()) {
                                HorizontalDivider(color = AppTheme.colors.textSecondary.copy(alpha = 0.1f), thickness = 1.dp)
                            }
                        }

                        // Botón Agregar Turno
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .background(Color.Transparent)
                                .border(1.dp, AppTheme.colors.primary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .clickable { viewModel.onEvent(WorkingHoursEvent.AddShift(Shift(name = "Nuevo Turno"))) },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Add, null, tint = AppTheme.colors.primary, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Agregar Turno", color = AppTheme.colors.primary, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun ShiftCard(
    shift: Shift,
    onToggleDay: (String) -> Unit,
    onDelete: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(shift.name, color = AppTheme.colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row {
                IconButton(onClick = { /* TODO: Edit Name */ }) {
                    Icon(Icons.Default.Edit, null, tint = AppTheme.colors.textSecondary, modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Outlined.Delete, null, tint = Color(0xFFEF5350).copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TimeDisplayBox(label = "INICIA", time = shift.startTime, modifier = Modifier.weight(1f))
            TimeDisplayBox(label = "FINALIZA", time = shift.endTime, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("DÍAS DEL TURNO", color = AppTheme.colors.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val days = listOf("L", "M", "X", "J", "V", "S", "D")
            days.forEach { day ->
                val isSelected = shift.days.contains(day)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            if (isSelected) AppTheme.colors.primary.copy(alpha = 0.2f) else AppTheme.colors.surface,
                            CircleShape
                        )
                        .border(
                            1.dp,
                            if (isSelected) AppTheme.colors.primary else AppTheme.colors.textSecondary.copy(alpha = 0.2f),
                            CircleShape
                        )
                        .clickable { onToggleDay(day) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        day,
                        color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TimeDisplayBox(label: String, time: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Surface(
            color = AppTheme.colors.surface,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(label, color = AppTheme.colors.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(time, color = AppTheme.colors.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
