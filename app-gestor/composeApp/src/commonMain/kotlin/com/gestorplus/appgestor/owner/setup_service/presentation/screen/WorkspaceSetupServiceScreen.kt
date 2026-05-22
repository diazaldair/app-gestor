package com.gestorplus.appgestor.owner.setup_service.presentation.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.owner.setup_service.presentation.state.DurationOption
import com.gestorplus.appgestor.owner.setup_service.presentation.state.WorkspaceSetupServiceEfffect
import com.gestorplus.appgestor.owner.setup_service.presentation.state.WorkspaceSetupServiceEvent
import com.gestorplus.appgestor.owner.setup_service.presentation.viewmodel.WorkspaceSetupServiceViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

private val DarkBgStart = Color(0xFF0F172A)
private val DarkBgEnd = Color(0xFF020617)
private val BrandBlue = Color(0xFF3B82F6)
private val BrandLightBlue = Color(0xFF60A5FA)
private val GlassBorder = Color(0xFF334155).copy(alpha = 0.4f)
private val InputFieldBg = Color(0xFF0F172A).copy(alpha = 0.6f)
private val CardBg = Color(0xFF1E293B).copy(alpha = 0.7f)
private val ChipBg = Color(0xFF3B82F6).copy(alpha = 0.15f)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkspaceSetupServiceScreen(
    onNavigateToNextStep: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: WorkspaceSetupServiceViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                WorkspaceSetupServiceEfffect.NavigateToNextStep -> onNavigateToNextStep()
                WorkspaceSetupServiceEfffect.NavigateBack -> onNavigateBack()
                is WorkspaceSetupServiceEfffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    DsTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    IconButton(onClick = { viewModel.onEvent(WorkspaceSetupServiceEvent.OnBackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Nuevo Servicio",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(listOf(DarkBgStart, DarkBgEnd)))
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                ) {
                    // Progress Bar
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.weight(1f).height(4.dp).clip(RoundedCornerShape(2.dp)).background(BrandBlue))
                        Box(modifier = Modifier.weight(1f).height(4.dp).clip(RoundedCornerShape(2.dp)).background(BrandBlue))
                        Box(modifier = Modifier.weight(1f).height(4.dp).clip(RoundedCornerShape(2.dp)).background(BrandLightBlue))
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PASO 3 DE 3",
                            color = BrandLightBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Podrás añadir más servicios desde tu panel una vez finalizado el registro.",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Field: Nombre del Servicio
                    Text("NOMBRE DEL SERVICIO", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.serviceName,
                        onValueChange = { viewModel.onEvent(WorkspaceSetupServiceEvent.ServiceNameChanged(it)) },
                        placeholder = { Text("Ej. Consulta Médica General", color = Color.White.copy(alpha = 0.3f)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = InputFieldBg,
                            unfocusedContainerColor = InputFieldBg,
                            unfocusedBorderColor = GlassBorder,
                            focusedBorderColor = BrandBlue,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.White.copy(alpha = 0.7f))
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Field: Descripción
                    Text("DESCRIPCIÓN", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.description,
                        onValueChange = { viewModel.onEvent(WorkspaceSetupServiceEvent.DescriptionChanged(it)) },
                        placeholder = { Text("Describe brevemente de qué trata este servicio...", color = Color.White.copy(alpha = 0.3f)) },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = InputFieldBg,
                            unfocusedContainerColor = InputFieldBg,
                            unfocusedBorderColor = GlassBorder,
                            focusedBorderColor = BrandBlue,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Row: Precio y Moneda
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("PRECIO", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = state.price,
                                onValueChange = { viewModel.onEvent(WorkspaceSetupServiceEvent.PriceChanged(it)) },
                                placeholder = { Text("0.00", color = Color.White.copy(alpha = 0.3f)) },
                                trailingIcon = { Text("$", color = Color.White.copy(alpha = 0.7f), modifier = Modifier.padding(end = 12.dp)) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = InputFieldBg,
                                    unfocusedContainerColor = InputFieldBg,
                                    unfocusedBorderColor = GlassBorder,
                                    focusedBorderColor = BrandBlue,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("MONEDA", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = state.currency,
                                onValueChange = { },
                                enabled = false,
                                trailingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White.copy(alpha = 0.3f)) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledContainerColor = InputFieldBg.copy(alpha = 0.4f),
                                    disabledBorderColor = GlassBorder.copy(alpha = 0.2f),
                                    disabledTextColor = Color.White.copy(alpha = 0.5f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Field: Duración del Servicio
                    Text("DURACIÓN DEL SERVICIO", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DurationOption.values().forEach { option ->
                            val isSelected = state.selectedDurationOption == option
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp)
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) ChipBg else InputFieldBg)
                                    .border(1.dp, if (isSelected) BrandBlue else GlassBorder, RoundedCornerShape(8.dp))
                                    .clickable { viewModel.onEvent(WorkspaceSetupServiceEvent.DurationOptionSelected(option)) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = option.label,
                                    color = if (isSelected) BrandLightBlue else Color.White.copy(alpha = 0.7f),
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Selector Personalizado
                    if (state.selectedDurationOption == DurationOption.CUSTOM) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(CardBg)
                                .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Horas
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("HORAS", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    IconButton(
                                        onClick = { viewModel.onEvent(WorkspaceSetupServiceEvent.DecrementCustomHours) },
                                        modifier = Modifier.size(32.dp).background(InputFieldBg, CircleShape)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                    Text(
                                        text = "${state.customHours}",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    IconButton(
                                        onClick = { viewModel.onEvent(WorkspaceSetupServiceEvent.IncrementCustomHours) },
                                        modifier = Modifier.size(32.dp).background(InputFieldBg, CircleShape)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                            
                            Box(modifier = Modifier.width(1.dp).height(40.dp).background(GlassBorder))
                            
                            // Minutos
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("MINUTOS", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    IconButton(
                                        onClick = { viewModel.onEvent(WorkspaceSetupServiceEvent.DecrementCustomMinutes) },
                                        modifier = Modifier.size(32.dp).background(InputFieldBg, CircleShape)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                    Text(
                                        text = "${state.customMinutes}",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    IconButton(
                                        onClick = { viewModel.onEvent(WorkspaceSetupServiceEvent.IncrementCustomMinutes) },
                                        modifier = Modifier.size(32.dp).background(InputFieldBg, CircleShape)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }

                    // Error text
                    state.errorMessage?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = { viewModel.onEvent(WorkspaceSetupServiceEvent.OnContinueClicked) },
                        enabled = !state.isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .padding(bottom = 32.dp)
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Finalizar",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
