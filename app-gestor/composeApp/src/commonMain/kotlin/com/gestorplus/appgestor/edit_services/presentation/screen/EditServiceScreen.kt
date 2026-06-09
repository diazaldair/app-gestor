package com.gestorplus.appgestor.edit_services.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
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
                    title = { Text("Editar Servicio $serviceId", color = AppTheme.colors.textPrimary, fontSize = 18.sp) },
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
                    // Placeholder para subir imagen
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
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
                    
                    Column {
                        Text("CATEGORÍA", color = AppTheme.colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = state.category,
                            onValueChange = { viewModel.onEvent(EditServiceEvent.CategoryChanged(it)) },
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, null, tint = AppTheme.colors.textSecondary) },
                            shape = RoundedCornerShape(12.dp),
                            colors = editFieldColors()
                        )
                    }

                    EditField(
                        label = "DESCRIPCIÓN", 
                        value = state.description, 
                        onValueChange = { viewModel.onEvent(EditServiceEvent.DescriptionChanged(it)) }, 
                        minLines = 3
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        EditField(
                            label = "PRECIO", 
                            value = state.price, 
                            onValueChange = { viewModel.onEvent(EditServiceEvent.PriceChanged(it)) }, 
                            modifier = Modifier.weight(1f),
                            trailingIcon = { Text(state.currency, color = AppTheme.colors.textSecondary) }
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
                            Text("Guardar Cambios", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AppTheme.colors.onPrimary)
                        }
                    }
                }
            }
        }
    }
}

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
