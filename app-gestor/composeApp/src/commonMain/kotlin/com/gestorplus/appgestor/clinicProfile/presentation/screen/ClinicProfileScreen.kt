package com.gestorplus.appgestor.clinicProfile.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.clinicProfile.presentation.state.ClinicProfileEvent
import com.gestorplus.appgestor.clinicProfile.presentation.viewmodel.ClinicProfileViewModel
import com.gestorplus.appgestor.designsystem.theme.DarkPalette
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ClinicProfileScreen(
    onBack: () -> Unit,
    viewModel: ClinicProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = DarkPalette.background,
        topBar = {
            ClinicProfileTopBar(onBack = onBack)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Ajustes de Perfil",
                color = DarkPalette.textPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Section: CLINIC PROFILE
            SectionHeader(icon = "🏥", title = "PERFIL DE LA CLÍNICA")
            
            ClinicTextField(
                label = "Nombre de la Clínica",
                value = uiState.profile.name,
                onValueChange = { viewModel.onEvent(ClinicProfileEvent.OnNameChanged(it)) }
            )

            ClinicTextField(
                label = "Biografía",
                value = uiState.profile.biography,
                onValueChange = { viewModel.onEvent(ClinicProfileEvent.OnBiographyChanged(it)) },
                singleLine = false,
                modifier = Modifier.height(120.dp)
            )

            Text(
                "Especialidades",
                color = DarkPalette.textSecondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            SpecialtiesFlowRow(
                specialties = uiState.profile.specialties,
                onRemove = { viewModel.onEvent(ClinicProfileEvent.OnRemoveSpecialty(it)) },
                onAdd = { viewModel.onEvent(ClinicProfileEvent.OnAddSpecialty("Nueva Especialidad")) } // Placeholder logic
            )

            Spacer(Modifier.height(24.dp))

            // Section: LOCATION
            SectionHeader(icon = "📍", title = "UBICACIÓN Y CONTACTO")
            
            // Map Placeholder
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1E293B)), contentAlignment = Alignment.Center) {
                    Text("MAPA INTERACTIVO", color = DarkPalette.textSecondary, fontSize = 12.sp)
                }
            }

            ClinicTextField(
                label = "Dirección exacta",
                value = uiState.profile.address,
                onValueChange = { viewModel.onEvent(ClinicProfileEvent.OnAddressChanged(it)) },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = DarkPalette.textSecondary) }
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { viewModel.onEvent(ClinicProfileEvent.OnSaveClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkPalette.primary.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("💾 Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun ClinicProfileTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )
    }
}

@Composable
fun SectionHeader(icon: String, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 12.dp)) {
        Text(icon, fontSize = 18.sp)
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            color = DarkPalette.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ClinicTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(label, color = DarkPalette.textSecondary, fontSize = 12.sp, modifier = Modifier.padding(bottom = 4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1E293B),
                unfocusedContainerColor = Color(0xFF1E293B),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = DarkPalette.primary,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = singleLine,
            leadingIcon = leadingIcon
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SpecialtiesFlowRow(
    specialties: List<String>,
    onRemove: (String) -> Unit,
    onAdd: () -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        specialties.forEach { specialty ->
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF1E293B),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2D3748))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(specialty, color = Color.White, fontSize = 14.sp)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "✕",
                        color = DarkPalette.textSecondary,
                        modifier = Modifier.clickable { onRemove(specialty) }
                    )
                }
            }
        }
        Surface(
            onClick = onAdd,
            shape = RoundedCornerShape(20.dp),
            color = Color.Transparent,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2D3748))
        ) {
            Text(
                "+ Añadir",
                color = DarkPalette.textSecondary,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                fontSize = 14.sp
            )
        }
    }
}
