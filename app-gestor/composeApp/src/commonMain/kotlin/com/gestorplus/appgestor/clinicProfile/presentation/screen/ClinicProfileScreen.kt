package com.gestorplus.appgestor.clinicProfile.presentation.screen

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.clinicProfile.presentation.state.ClinicProfileEfffect
import com.gestorplus.appgestor.clinicProfile.presentation.state.ClinicProfileEvent
import com.gestorplus.appgestor.clinicProfile.presentation.viewmodel.ClinicProfileViewModel
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.resources.stringResource
import app_gestor.composeapp.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ClinicProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: ClinicProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.efffect.collect { effect ->
            when (effect) {
                ClinicProfileEfffect.NavigateBack -> onNavigateBack()
                is ClinicProfileEfffect.ShowMessage -> { /* Show Snackbar */ }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.clinic_profile_title), style = AppTheme.typography.headlineLarge.copy(fontSize = 20.sp)) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(ClinicProfileEvent.BackClicked) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier.padding(end = 16.dp).size(40.dp).clip(CircleShape).background(Color.Gray)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = AppTheme.colors.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Section Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.BusinessCenter, contentDescription = null, tint = AppTheme.colors.primary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(Res.string.clinic_profile_section_info), color = AppTheme.colors.primary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            ClinicInputField(
                label = stringResource(Res.string.clinic_profile_name_label),
                value = state.profile.name,
                onValueChange = { viewModel.onEvent(ClinicProfileEvent.NameChanged(it)) }
            )

            ClinicInputField(
                label = stringResource(Res.string.clinic_profile_bio_label),
                value = state.profile.biography,
                onValueChange = { viewModel.onEvent(ClinicProfileEvent.BiographyChanged(it)) },
                modifier = Modifier.height(120.dp),
                singleLine = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(stringResource(Res.string.clinic_profile_specialties_label), color = AppTheme.colors.textSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.profile.specialties.forEach { speciality ->
                    SpecialityChip(
                        name = speciality,
                        onRemove = { viewModel.onEvent(ClinicProfileEvent.RemoveSpecialityClicked(speciality)) }
                    )
                }
                
                OutlinedCard(
                    onClick = { viewModel.onEvent(ClinicProfileEvent.AddSpecialityClicked) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(Res.string.clinic_profile_add_specialty), color = AppTheme.colors.textSecondary, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Location Section
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = AppTheme.colors.primary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(Res.string.clinic_profile_section_location), color = AppTheme.colors.primary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mock Map
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppTheme.colors.surface),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(Res.string.clinic_profile_map_placeholder), color = AppTheme.colors.textSecondary, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            ClinicInputField(
                label = stringResource(Res.string.clinic_profile_address_label),
                value = state.profile.address,
                onValueChange = { viewModel.onEvent(ClinicProfileEvent.AddressChanged(it)) },
                leadingIcon = { Icon(Icons.Default.Map, contentDescription = null, tint = AppTheme.colors.textSecondary) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.onEvent(ClinicProfileEvent.SaveClicked) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary)
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(Res.string.clinic_profile_save_button), fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ClinicInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(label, color = AppTheme.colors.textSecondary, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = AppTheme.colors.surface,
                unfocusedContainerColor = AppTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = singleLine,
            leadingIcon = leadingIcon
        )
    }
}

@Composable
fun SpecialityChip(name: String, onRemove: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = AppTheme.colors.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppTheme.colors.textSecondary.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(name, color = AppTheme.colors.textPrimary, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = onRemove, modifier = Modifier.size(16.dp)) {
                Icon(Icons.Default.Close, contentDescription = "Remove", modifier = Modifier.size(12.dp))
            }
        }
    }
}
