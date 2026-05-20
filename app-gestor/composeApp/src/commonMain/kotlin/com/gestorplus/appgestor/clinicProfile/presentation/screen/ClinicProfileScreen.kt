package com.gestorplus.appgestor.clinicProfile.presentation.screen

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.clinicProfile.presentation.state.ClinicProfileEvent
import com.gestorplus.appgestor.clinicProfile.presentation.viewmodel.ClinicProfileViewModel
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ClinicProfileScreen(
    onBack: () -> Unit,
    viewModel: ClinicProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = AppTheme.colors.background,
        topBar = {
            ClinicProfileTopBar(onBack = onBack)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Ajustes de Perfil",
                color = AppTheme.colors.textPrimary,
                style = AppTheme.typography.headlineLarge.copy(fontSize = 22.sp),
                modifier = Modifier.padding(vertical = 20.dp)
            )

            // Section: CLINIC PROFILE
            SectionHeader(icon = Icons.Default.BusinessCenter, title = "PERFIL DE LA CLÍNICA")
            
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
                modifier = Modifier.height(130.dp)
            )

            Text(
                "Especialidades",
                color = AppTheme.colors.textSecondary,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
            )
            SpecialtiesFlowRow(
                specialties = uiState.profile.specialties,
                onRemove = { viewModel.onEvent(ClinicProfileEvent.OnRemoveSpecialty(it)) },
                onAdd = { viewModel.onEvent(ClinicProfileEvent.OnAddSpecialty("Nueva Especialidad")) }
            )

            Spacer(Modifier.height(28.dp))

            // Section: LOCATION
            SectionHeader(icon = Icons.Default.LocationOn, title = "UBICACIÓN Y CONTACTO")
            
            // Map Placeholder similar to Figma
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    // Aquí iría el componente de mapa interactivo real
                    Text(
                        "MAPA INTERACTIVO", 
                        color = AppTheme.colors.textSecondary, 
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            ClinicTextField(
                label = "Dirección exacta",
                value = uiState.profile.address,
                onValueChange = { viewModel.onEvent(ClinicProfileEvent.OnAddressChanged(it)) },
                leadingIcon = { Icon(Icons.Default.Map, contentDescription = null, tint = AppTheme.colors.textSecondary, modifier = Modifier.size(20.dp)) }
            )

            Spacer(Modifier.height(40.dp))

            Button(
                onClick = { viewModel.onEvent(ClinicProfileEvent.OnSaveClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary),
                shape = RoundedCornerShape(14.dp)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(color = AppTheme.colors.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Guardar Cambios", color = AppTheme.colors.onPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
            
            Spacer(Modifier.height(40.dp))
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
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = AppTheme.colors.textPrimary)
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(AppTheme.colors.surface),
            contentAlignment = Alignment.Center
        ) {
             Icon(Icons.Default.Person, contentDescription = null, tint = AppTheme.colors.textSecondary)
        }
    }
}

@Composable
fun SectionHeader(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 12.dp)) {
        Icon(icon, contentDescription = null, tint = AppTheme.colors.primary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        Text(
            text = title,
            color = AppTheme.colors.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp
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
        Text(
            label, 
            color = AppTheme.colors.textSecondary, 
            fontSize = 13.sp, 
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = AppTheme.colors.surface,
                unfocusedContainerColor = AppTheme.colors.surface,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = AppTheme.colors.primary,
                focusedTextColor = AppTheme.colors.textPrimary,
                unfocusedTextColor = AppTheme.colors.textPrimary
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
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        specialties.forEach { specialty ->
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = AppTheme.colors.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.colors.textSecondary.copy(alpha = 0.3f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(specialty, color = AppTheme.colors.textPrimary, fontSize = 14.sp)
                    Spacer(Modifier.width(6.dp))
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        tint = AppTheme.colors.textSecondary,
                        modifier = Modifier.size(14.dp).clickable { onRemove(specialty) }
                    )
                }
            }
        }
        Surface(
            onClick = onAdd,
            shape = RoundedCornerShape(22.dp),
            color = Color.Transparent,
            border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.colors.textSecondary.copy(alpha = 0.3f))
        ) {
            Text(
                "+ Añadir",
                color = AppTheme.colors.textSecondary,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                fontSize = 14.sp
            )
        }
    }
}
