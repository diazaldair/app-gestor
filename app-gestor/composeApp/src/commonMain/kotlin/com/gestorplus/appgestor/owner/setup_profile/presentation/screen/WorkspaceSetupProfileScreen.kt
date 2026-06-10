package com.gestorplus.appgestor.owner.setup_profile.presentation.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.owner.setup_profile.presentation.state.WorkspaceSetupProfileEfffect
import com.gestorplus.appgestor.owner.setup_profile.presentation.state.WorkspaceSetupProfileEvent
import com.gestorplus.appgestor.owner.setup_profile.presentation.viewmodel.WorkspaceSetupProfileViewModel
import com.gestorplus.appgestor.util.rememberImagePicker
import com.gestorplus.appgestor.core.util.ImageKitConfig
import coil3.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
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

@Composable
fun WorkspaceSetupProfileScreen(
    onNavigateToNextStep: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: WorkspaceSetupProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val imagePicker = rememberImagePicker { uri ->
        viewModel.onEvent(WorkspaceSetupProfileEvent.PhotoSelected(uri))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                WorkspaceSetupProfileEfffect.NavigateToServices -> onNavigateToNextStep()
                WorkspaceSetupProfileEfffect.NavigateBack -> onNavigateBack()
                is WorkspaceSetupProfileEfffect.ShowSnackbar -> {
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
                    IconButton(onClick = { viewModel.onEvent(WorkspaceSetupProfileEvent.OnBackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Perfil Profesional",
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
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(DarkBgStart, DarkBgEnd)
                        )
                    )
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                ) {
                    // Step progress
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(48.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(BrandLightBlue)
                        )
                        Box(
                            modifier = Modifier
                                .width(32.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color(0xFF334155))
                        )
                        Box(
                            modifier = Modifier
                                .width(32.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color(0xFF334155))
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Input 1: NOMBRE DE LA CLÍNICA
                    Text(
                        text = "NOMBRE DE LA CLÍNICA / CONSULTORIO",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value = state.clinicName,
                        onValueChange = { viewModel.onEvent(WorkspaceSetupProfileEvent.ClinicNameChanged(it)) },
                        placeholder = { Text("Ej. Clínica Dental Sonrisas", color = Color.White.copy(alpha = 0.3f)) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandLightBlue,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = InputFieldBg,
                            unfocusedContainerColor = InputFieldBg
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Input 2: NOMBRE COMPLETO
                    Text(
                        text = "NOMBRE COMPLETO",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value = state.fullName,
                        onValueChange = { viewModel.onEvent(WorkspaceSetupProfileEvent.FullNameChanged(it)) },
                        placeholder = { Text("Ej. Dr. Julián Castro", color = Color.White.copy(alpha = 0.3f)) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandLightBlue,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = InputFieldBg,
                            unfocusedContainerColor = InputFieldBg
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Especialidades
                    Text(
                        text = "ESPECIALIDADES",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(CardBg)
                            .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            state.specialities.forEach { spec ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(ChipBg)
                                        .border(1.dp, BrandBlue.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = spec,
                                        color = BrandLightBlue,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Borrar",
                                        tint = BrandLightBlue,
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clickable {
                                                viewModel.onEvent(WorkspaceSetupProfileEvent.RemoveSpecialityClicked(spec))
                                            }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = state.inputSpeciality,
                            onValueChange = { viewModel.onEvent(WorkspaceSetupProfileEvent.InputSpecialityChanged(it)) },
                            placeholder = { Text("Añadir especialidad...", color = Color.White.copy(alpha = 0.3f)) },
                            trailingIcon = {
                                IconButton(onClick = { viewModel.onEvent(WorkspaceSetupProfileEvent.AddSpecialityClicked) }) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Añadir",
                                        tint = Color.White
                                    )
                                }
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Biografía
                    Text(
                        text = "BIOGRAFÍA",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value = state.biography,
                        onValueChange = { viewModel.onEvent(WorkspaceSetupProfileEvent.BiographyChanged(it)) },
                        placeholder = {
                            Text(
                                "Describe brevemente la trayectoria...",
                                color = Color.White.copy(alpha = 0.3f),
                                fontSize = 14.sp
                            )
                        },
                        minLines = 3,
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandLightBlue,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = InputFieldBg,
                            unfocusedContainerColor = InputFieldBg
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // UBICACIÓN (Link en vez de mapa)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "UBICACIÓN Y DEPARTAMENTO",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        
                        var deptExpanded by remember { mutableStateOf(false) }
                        Box {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { deptExpanded = true }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = BrandLightBlue,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = state.selectedDepartment,
                                    color = BrandLightBlue,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            DropdownMenu(
                                expanded = deptExpanded,
                                onDismissRequest = { deptExpanded = false },
                                modifier = Modifier.background(CardBg).border(1.dp, GlassBorder)
                            ) {
                                listOf("La Paz", "Santa Cruz", "Cochabamba", "Oruro", "Potosí", "Sucre", "Tarija", "Beni", "Pando").forEach { dept ->
                                    DropdownMenuItem(
                                        text = { Text(dept, color = Color.White) },
                                        onClick = {
                                            viewModel.onEvent(WorkspaceSetupProfileEvent.DepartmentSelected(dept))
                                            deptExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // CAMPO DE ENLACE DE GOOGLE MAPS (NUEVO)
                    OutlinedTextField(
                        value = state.locationUrl,
                        onValueChange = { viewModel.onEvent(WorkspaceSetupProfileEvent.LocationUrlChanged(it)) },
                        placeholder = { Text("Enlace de Google Maps (Opcional)", color = Color.White.copy(alpha = 0.3f)) },
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Link, contentDescription = null, tint = BrandLightBlue)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandLightBlue,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = InputFieldBg,
                            unfocusedContainerColor = InputFieldBg
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dirección exacta
                    OutlinedTextField(
                        value = state.exactAddress,
                        onValueChange = { viewModel.onEvent(WorkspaceSetupProfileEvent.ExactAddressChanged(it)) },
                        placeholder = { Text("Dirección exacta (Av. Arce #123)", color = Color.White.copy(alpha = 0.3f)) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandLightBlue,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = InputFieldBg,
                            unfocusedContainerColor = InputFieldBg
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Referencias
                    OutlinedTextField(
                        value = state.references,
                        onValueChange = { viewModel.onEvent(WorkspaceSetupProfileEvent.ReferencesChanged(it)) },
                        placeholder = { Text("Referencia (Edf. Multicentro, Piso 4)", color = Color.White.copy(alpha = 0.3f)) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandLightBlue,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = InputFieldBg,
                            unfocusedContainerColor = InputFieldBg
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Galería
                    Text(
                        text = "GALERÍA DEL CONSULTORIO",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(InputFieldBg)
                                    .clickable { imagePicker.pickImage() }
                                    .drawBehind {
                                        drawRoundRect(
                                            color = GlassBorder,
                                            style = Stroke(
                                                width = 2f,
                                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                            ),
                                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx(), 12.dp.toPx())
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.PhotoCamera, null, tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("AÑADIR FOTO", color = Color.White.copy(alpha = 0.4f), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        items(state.galleryImages) { image ->
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF334155))
                                    .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                            ) {
                                AsyncImage(
                                    model = ImageKitConfig.getOptimizedUrl(image, width = 200),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(Color.Red)
                                        .clickable { viewModel.onEvent(WorkspaceSetupProfileEvent.RemovePhotoClicked(image)) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.size(10.dp))
                                }
                            }
                        }
                    }

                    state.errorMessage?.let { error ->
                        Text(text = error, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.padding(top = 16.dp))
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Footer y Botón Continuar
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("PASO 1 DE 3", color = Color.White.copy(alpha = 0.4f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("Información Básica", color = BrandLightBlue, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = { viewModel.onEvent(WorkspaceSetupProfileEvent.OnContinueClicked) },
                        enabled = !state.isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp).height(54.dp)
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Continuar a Servicios", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.Default.ArrowForward, null, tint = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
