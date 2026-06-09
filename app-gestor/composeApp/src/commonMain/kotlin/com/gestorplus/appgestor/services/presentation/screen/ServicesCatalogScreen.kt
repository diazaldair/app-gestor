package com.gestorplus.appgestor.services.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.services.domain.model.ServiceModel
import com.gestorplus.appgestor.services.presentation.viewmodel.ServicesViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesCatalogScreen(
    onBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    onOpenMenu: () -> Unit,
    viewModel: ServicesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    DsTheme {
        Scaffold(
            containerColor = AppTheme.colors.background,
            topBar = {
                TopAppBar(
                    title = { Text("Servicios", color = AppTheme.colors.textPrimary, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = AppTheme.colors.textPrimary)
                        }
                    },
                    actions = {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(AppTheme.colors.textSecondary.copy(alpha = 0.5f))
                        )
                        Spacer(Modifier.width(16.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("CATÁLOGO MÉDICO", color = AppTheme.colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("Gestión de servicios", color = AppTheme.colors.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("${uiState.services.size} servicios activos", color = AppTheme.colors.textSecondary, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { /* TODO: Implement search in ViewModel */ },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar servicio...", color = AppTheme.colors.textSecondary) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = AppTheme.colors.textSecondary) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = AppTheme.colors.surface,
                        focusedContainerColor = AppTheme.colors.surface,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = AppTheme.colors.primary,
                        focusedTextColor = AppTheme.colors.textPrimary,
                        unfocusedTextColor = AppTheme.colors.textPrimary
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (uiState.services.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No tienes servicios registrados", color = AppTheme.colors.textSecondary)
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(uiState.services) { service ->
                            ServiceItemCard(
                                service = service,
                                onEdit = { onNavigateToEdit(service.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceItemCard(service: ServiceModel, onEdit: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(service.name, color = AppTheme.colors.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Switch(
                    checked = service.isActive,
                    onCheckedChange = { },
                    colors = SwitchDefaults.colors(checkedTrackColor = AppTheme.colors.primary)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccessTime, null, tint = AppTheme.colors.textSecondary, modifier = Modifier.size(14.dp))
                Text(" ${service.durationMinutes} min", color = AppTheme.colors.textSecondary, fontSize = 14.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .background(AppTheme.colors.background, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("COSTO TOTAL", color = AppTheme.colors.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("${service.price.toInt()} Bs", color = AppTheme.colors.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                
                TextButton(onClick = onEdit) {
                    Text("EDITAR", color = AppTheme.colors.primary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
