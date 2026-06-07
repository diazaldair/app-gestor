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
import com.gestorplus.appgestor.services.domain.model.ServiceModel
import com.gestorplus.appgestor.services.presentation.state.ServicesCatalogUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesCatalogScreen(
    onBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    onOpenMenu: () -> Unit
) {
    // Simulated state for UI development
    val uiState = ServicesCatalogUiState(
        services = listOf(
            ServiceModel(id = "1", name = "Limpieza Dental Pro", durationMinutes = 45, price = 350.0, isActive = true),
            ServiceModel(id = "2", name = "Consulta General", durationMinutes = 30, price = 200.0, isActive = true)
        )
    )

    Scaffold(
        containerColor = Color(0xFF0F172A),
        topBar = {
            TopAppBar(
                title = { Text("Servicios", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onOpenMenu) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
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
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("CATÁLOGO MÉDICO", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Gestión de servicios", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("${uiState.services.size} servicios activos", color = Color.Gray, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar servicio...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF1E293B),
                    focusedContainerColor = Color(0xFF1E293B),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color(0xFF3B82F6),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

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

@Composable
fun ServiceItemCard(service: ServiceModel, onEdit: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(service.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Switch(
                    checked = service.isActive,
                    onCheckedChange = { },
                    colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF3B82F6))
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccessTime, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                Text(" ${service.durationMinutes} min", color = Color.Gray, fontSize = 14.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .background(Color(0xFF0F172A), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("COSTO TOTAL", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("${service.price.toInt()} Bs", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                
                TextButton(onClick = onEdit) {
                    Text("EDITAR", color = Color(0xFF3B82F6), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
