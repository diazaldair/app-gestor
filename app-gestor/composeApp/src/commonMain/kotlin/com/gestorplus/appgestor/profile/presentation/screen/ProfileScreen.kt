package com.gestorplus.appgestor.profile.presentation.screen

import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.profile.presentation.viewmodel.ProfileViewModel
import com.gestorplus.appgestor.profile.presentation.state.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.resources.stringResource
import app_gestor.composeapp.generated.resources.*

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ProfileEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                ProfileEffect.NavigateNext -> {
                    onBack()
                }
            }
        }
    }

    DsTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                ProfileTopBar(
                    isEditing = state.isEditing,
                    onBack = onBack,
                    onToggleEdit = { viewModel.onEvent(ProfileEvent.ToggleEditMode) },
                    onSave = { viewModel.onEvent(ProfileEvent.SaveProfile) }
                )
            },
            containerColor = AppTheme.colors.background
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = AppTheme.colors.primary
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProfileAvatarHeader(
                            name = state.name,
                            isEditing = state.isEditing,
                            imageUrl = state.imageUrl,
                            onImageUrlChanged = { viewModel.onEvent(ProfileEvent.ImageUrlChanged(it)) }
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        ProfileFormSection(
                            state = state,
                            onNameChanged = { viewModel.onEvent(ProfileEvent.NameChanged(it)) },
                            onEmailChanged = { viewModel.onEvent(ProfileEvent.EmailChanged(it)) },
                            onPhoneChanged = { viewModel.onEvent(ProfileEvent.PhoneChanged(it)) },
                            onDescriptionChanged = { viewModel.onEvent(ProfileEvent.DescriptionChanged(it)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileTopBar(
    isEditing: Boolean,
    onBack: () -> Unit,
    onToggleEdit: () -> Unit,
    onSave: () -> Unit
) {
    Surface(
        color = AppTheme.colors.background,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, AppTheme.colors.textPrimary.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = AppTheme.colors.textPrimary
                )
            }

            Text(
                text = "Mi Perfil Profesional",
                color = AppTheme.colors.textPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            if (isEditing) {
                IconButton(onClick = onSave) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Guardar",
                        tint = AppTheme.colors.primary
                    )
                }
            } else {
                IconButton(onClick = onToggleEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = AppTheme.colors.textPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileAvatarHeader(
    name: String,
    isEditing: Boolean,
    imageUrl: String,
    onImageUrlChanged: (String) -> Unit
) {
    val initials = name.split(" ")
        .filter { it.isNotEmpty() }
        .take(2)
        .map { it.first().uppercase() }
        .joinToString("")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(AppTheme.colors.primary, AppTheme.colors.primary.copy(alpha = 0.6f))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials.ifEmpty { "DP" },
                color = AppTheme.colors.onPrimary,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
            
            if (isEditing) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Cambiar Foto",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = name.ifEmpty { "Administrador" },
            color = AppTheme.colors.textPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Dueño de Negocio / Profesional",
            color = AppTheme.colors.textSecondary,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ProfileFormSection(
    state: ProfileState,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Información General",
            color = AppTheme.colors.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        if (state.isEditing) {
            OutlinedTextField(
                value = state.name,
                onValueChange = onNameChanged,
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppTheme.colors.primary,
                    unfocusedBorderColor = AppTheme.colors.textSecondary.copy(alpha = 0.5f),
                    focusedLabelColor = AppTheme.colors.primary,
                    unfocusedLabelColor = AppTheme.colors.textSecondary,
                    focusedTextColor = AppTheme.colors.textPrimary,
                    unfocusedTextColor = AppTheme.colors.textPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChanged,
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppTheme.colors.primary,
                    unfocusedBorderColor = AppTheme.colors.textSecondary.copy(alpha = 0.5f),
                    focusedLabelColor = AppTheme.colors.primary,
                    unfocusedLabelColor = AppTheme.colors.textSecondary,
                    focusedTextColor = AppTheme.colors.textPrimary,
                    unfocusedTextColor = AppTheme.colors.textPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = state.phone,
                onValueChange = onPhoneChanged,
                label = { Text("Teléfono de Contacto") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppTheme.colors.primary,
                    unfocusedBorderColor = AppTheme.colors.textSecondary.copy(alpha = 0.5f),
                    focusedLabelColor = AppTheme.colors.primary,
                    unfocusedLabelColor = AppTheme.colors.textSecondary,
                    focusedTextColor = AppTheme.colors.textPrimary,
                    unfocusedTextColor = AppTheme.colors.textPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = state.description,
                onValueChange = onDescriptionChanged,
                label = { Text("Descripción Profesional") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppTheme.colors.primary,
                    unfocusedBorderColor = AppTheme.colors.textSecondary.copy(alpha = 0.5f),
                    focusedLabelColor = AppTheme.colors.primary,
                    unfocusedLabelColor = AppTheme.colors.textSecondary,
                    focusedTextColor = AppTheme.colors.textPrimary,
                    unfocusedTextColor = AppTheme.colors.textPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            )
        } else {
            ProfileDetailItem(
                icon = Icons.Default.Person,
                label = "Nombre Completo",
                value = state.name
            )

            ProfileDetailItem(
                icon = Icons.Default.Email,
                label = "Correo Electrónico",
                value = state.email
            )

            ProfileDetailItem(
                icon = Icons.Default.Phone,
                label = "Teléfono de Contacto",
                value = state.phone
            )

            ProfileDetailItem(
                icon = Icons.Default.Description,
                label = "Biografía y Descripción",
                value = state.description
            )
        }
    }
}

@Composable
fun ProfileDetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(AppTheme.colors.primary.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AppTheme.colors.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    color = AppTheme.colors.textSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value.ifEmpty { "No especificado" },
                    color = AppTheme.colors.textPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
