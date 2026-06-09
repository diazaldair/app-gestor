package com.gestorplus.appgestor.edit_services.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import com.gestorplus.appgestor.edit_services.presentation.state.EditServiceEvent
import com.gestorplus.appgestor.edit_services.presentation.viewmodel.EditServiceViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditServiceScreen(
    onBack: () -> Unit,
    serviceId: String,
    viewModel: EditServiceViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(serviceId) {
        viewModel.loadService(serviceId)
    }

    DsTheme {
        Scaffold(
            containerColor = AppTheme.colors.background,
            topBar = {
                TopAppBar(
                    title = { Text("Editar Servicio", color = AppTheme.colors.textPrimary, fontSize = 18.sp) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = AppTheme.colors.textPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
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
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Placeholder para subir imagen (opcional según la imagen, pero lo mantenemos si estaba)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .background(AppTheme.colors.surface.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .border(1.dp, AppTheme.colors.textSecondary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .clickable { /* TODO */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.AddAPhoto, null, tint = AppTheme.colors.textSecondary, modifier = Modifier.size(32.dp))
                            Spacer(Modifier.height(8.dp))
                            Text("Subir imagen de portada", color = AppTheme.colors.textSecondary, fontSize = 14.sp)
                        }
                    }

                    EditField(
                        label = "NOMBRE DEL SERVICIO", 
                        value = state.name, 
                        onValueChange = { viewModel.onEvent(EditServiceEvent.NameChanged(it)) }
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        EditField(
                            label = "PRECIO", 
                            value = state.price, 
                            onValueChange = { viewModel.onEvent(EditServiceEvent.PriceChanged(it)) }, 
                            modifier = Modifier.weight(1f),
                            trailingIcon = { Text("$", color = AppTheme.colors.textSecondary) }
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text("MONEDA", color = AppTheme.colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = state.currency,
                                onValueChange = { },
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth(),
                                trailingIcon = { Icon(Icons.Default.Lock, null, tint = AppTheme.colors.textSecondary, modifier = Modifier.size(16.dp)) },
                                shape = RoundedCornerShape(12.dp),
                                colors = editFieldColors()
                            )
                        }
                    }

                    DurationSelector(
                        hours = state.hours,
                        minutes = state.minutes,
                        onDurationChange = { h, m -> viewModel.onEvent(EditServiceEvent.DurationChanged(h, m)) }
                    )

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = { viewModel.onEvent(EditServiceEvent.SaveService, onSuccess = onBack) },
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
                                Spacer(Modifier.width(8.dp))
                                Text("Guardar Cambios", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }

                    TextButton(
                        onClick = { viewModel.onEvent(EditServiceEvent.DeleteService, onSuccess = onBack) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFEF5350))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Delete, null, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Eliminar Servicio", fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DurationSelector(
    hours: Int,
    minutes: Int,
    onDurationChange: (Int, Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("DURACIÓN DEL SERVICIO", color = AppTheme.colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val options = listOf("15m", "30m", "45m", "60m", "Pers.")
            options.forEach { option ->
                val isSelected = when (option) {
                    "15m" -> hours == 0 && minutes == 15
                    "30m" -> hours == 0 && minutes == 30
                    "45m" -> hours == 0 && minutes == 45
                    "60m" -> hours == 1 && minutes == 0
                    else -> (hours > 0 || (minutes != 15 && minutes != 30 && minutes != 45)) && minutes != 0
                }
                
                Surface(
                    onClick = {
                        when (option) {
                            "15m" -> onDurationChange(0, 15)
                            "30m" -> onDurationChange(0, 30)
                            "45m" -> onDurationChange(0, 45)
                            "60m" -> onDurationChange(1, 0)
                        }
                    },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) AppTheme.colors.primary.copy(alpha = 0.1f) else AppTheme.colors.surface,
                    border = borderStroke(isSelected)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = option,
                            color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textPrimary,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.surface, RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Horas
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("HORAS", color = AppTheme.colors.textSecondary, fontSize = 10.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { if (hours > 0) onDurationChange(hours - 1, minutes) }) {
                        Icon(Icons.Default.RemoveCircleOutline, null, tint = AppTheme.colors.textPrimary)
                    }
                    Text(hours.toString(), color = AppTheme.colors.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { onDurationChange(hours + 1, minutes) }) {
                        Icon(Icons.Default.AddCircleOutline, null, tint = AppTheme.colors.textPrimary)
                    }
                }
            }
            
            Box(modifier = Modifier.width(1.dp).height(40.dp).background(AppTheme.colors.textSecondary.copy(alpha = 0.2f)))

            // Minutos
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("MINUTOS", color = AppTheme.colors.textSecondary, fontSize = 10.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { if (minutes >= 5) onDurationChange(hours, minutes - 5) else if (hours > 0) onDurationChange(hours - 1, 55) }) {
                        Icon(Icons.Default.RemoveCircleOutline, null, tint = AppTheme.colors.textPrimary)
                    }
                    Text(minutes.toString(), color = AppTheme.colors.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { if (minutes < 55) onDurationChange(hours, minutes + 5) else onDurationChange(hours + 1, 0) }) {
                        Icon(Icons.Default.AddCircleOutline, null, tint = AppTheme.colors.textPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun borderStroke(isSelected: Boolean) = androidx.compose.foundation.BorderStroke(
    width = 1.dp,
    color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textSecondary.copy(alpha = 0.2f)
)

@Composable
fun EditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        Text(label, color = AppTheme.colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            minLines = minLines,
            shape = RoundedCornerShape(12.dp),
            colors = editFieldColors(),
            trailingIcon = trailingIcon
        )
    }
}

@Composable
fun editFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedContainerColor = AppTheme.colors.surface,
    focusedContainerColor = AppTheme.colors.surface,
    unfocusedBorderColor = Color.Transparent,
    focusedBorderColor = AppTheme.colors.primary,
    focusedTextColor = AppTheme.colors.textPrimary,
    unfocusedTextColor = AppTheme.colors.textPrimary
)
