package com.gestorplus.appgestor.clinic_detail.presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.gestorplus.appgestor.clinic_detail.domain.model.ClinicService
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicDetailScreen(
    clinicId: String,
    onNavigateBack: () -> Unit,
    onNavigateToBooking: (String, String) -> Unit,
    viewModel: ClinicDetailViewModel = koinViewModel { parametersOf(clinicId) }
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ClinicDetailEffect.NavigateBack -> onNavigateBack()
                is ClinicDetailEffect.NavigateToBooking -> onNavigateToBooking(effect.clinicId, effect.serviceId)
            }
        }
    }

    DsTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "SoloBook",
                            style = AppTheme.typography.headlineLarge.copy(
                                fontSize = 18.sp,
                                color = AppTheme.colors.textPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.onEvent(ClinicDetailEvent.OnBackClicked) }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = AppTheme.colors.textPrimary
                            )
                        }
                    },
                    actions = {
                        Box(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.05f))
                                .border(1.dp, Color.White.copy(alpha = 0.05f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MedicalServices,
                                contentDescription = null,
                                tint = AppTheme.colors.textPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = AppTheme.colors.background.copy(alpha = 0.9f)
                    )
                )
            },
            containerColor = AppTheme.colors.background
        ) { paddingValues ->
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppTheme.colors.primary)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    HeroSection(state.clinic?.name ?: "", state.clinic?.imageUrl)
                    
                    Column(modifier = Modifier.padding(16.dp)) {
                        BioSection(state.clinic?.description ?: "No hay descripción disponible.")
                        
                        SpecialtiesSection(state.clinic?.specialties ?: emptyList())
                        
                        LocationSection(state.clinic?.address ?: "Ubicación no disponible")
                        
                        ServicesSection(
                            services = state.services,
                            onBookClick = { serviceId -> 
                                viewModel.onEvent(ClinicDetailEvent.OnBookServiceClicked(serviceId))
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun HeroSection(name: String, imageUrl: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        AsyncImage(
            model = imageUrl ?: "https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?q=80&w=1000&auto=format&fit=crop",
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, AppTheme.colors.background.copy(alpha = 0.8f)),
                        startY = 300f
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Text(
                text = name,
                style = AppTheme.typography.headlineLarge.copy(
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "Centro de Especialidades Médicas",
                style = AppTheme.typography.bodyMedium.copy(
                    color = AppTheme.colors.primary,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
fun BioSection(bio: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Text(
            "Bio",
            style = AppTheme.typography.headlineLarge.copy(
                fontSize = 18.sp,
                color = AppTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            bio,
            style = AppTheme.typography.bodyMedium.copy(
                color = AppTheme.colors.textSecondary,
                lineHeight = 22.sp
            )
        )
    }
}

@Composable
fun SpecialtiesSection(specialties: List<String>) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Text(
            "Especialidades",
            style = AppTheme.typography.headlineLarge.copy(
                fontSize = 18.sp,
                color = AppTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            specialties.forEach { specialty ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.05f))
                        .border(1.dp, Color.White.copy(alpha = 0.05f), CircleShape)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        specialty,
                        style = AppTheme.typography.bodySmall.copy(
                            color = AppTheme.colors.textSecondary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun LocationSection(address: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Text(
            "Ubicación",
            style = AppTheme.typography.headlineLarge.copy(
                fontSize = 18.sp,
                color = AppTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.Top) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = AppTheme.colors.primary,
                modifier = Modifier.size(20.dp).padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    address,
                    style = AppTheme.typography.bodyMedium.copy(color = AppTheme.colors.textSecondary)
                )
                Text(
                    "Ver en el mapa",
                    modifier = Modifier.clickable { /* TODO */ }.padding(vertical = 4.dp),
                    style = AppTheme.typography.bodySmall.copy(
                        color = AppTheme.colors.textSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

@Composable
fun ServicesSection(services: List<ClinicService>, onBookClick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Text(
            "Servicios",
            style = AppTheme.typography.headlineLarge.copy(
                fontSize = 18.sp,
                color = AppTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        services.forEach { service ->
            ServiceCard(service = service, onBookClick = onBookClick)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ServiceCard(service: ClinicService, onBookClick: (String) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.colors.surface,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    service.name,
                    style = AppTheme.typography.bodyLarge.copy(
                        color = AppTheme.colors.textPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                )
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = AppTheme.colors.textSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${service.durationMinutes} min",
                        style = AppTheme.typography.bodySmall.copy(color = AppTheme.colors.textSecondary, fontSize = 12.sp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("|", color = AppTheme.colors.textSecondary.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Ver descripción",
                        modifier = Modifier.clickable { /* TODO */ },
                        style = AppTheme.typography.bodySmall.copy(
                            color = AppTheme.colors.textSecondary,
                            fontSize = 11.sp
                        )
                    )
                }
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "$${service.price}",
                    style = AppTheme.typography.headlineLarge.copy(
                        fontSize = 18.sp,
                        color = AppTheme.colors.textPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { onBookClick(service.id) },
                    shape = CircleShape,
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(
                        "Reservar",
                        style = AppTheme.typography.bodySmall.copy(
                            color = AppTheme.colors.textPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}
