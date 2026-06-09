package com.gestorplus.appgestor.explore_clinics.presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.gestorplus.appgestor.clinicProfile.domain.model.Clinic
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreClinicsScreen(
    onNavigateToClinicDetail: (String) -> Unit,
    onNavigateToMyAppointments: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: ExploreClinicsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    DsTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MedicalServices,
                                contentDescription = null,
                                tint = AppTheme.colors.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "SoloBook Health",
                                style = AppTheme.typography.headlineLarge.copy(
                                    fontSize = 18.sp,
                                    color = AppTheme.colors.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: Notifications */ }) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = AppTheme.colors.textSecondary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AppTheme.colors.background
                    )
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    currentScreen = "Explorar",
                    onExplorarClick = { },
                    onCitasClick = onNavigateToMyAppointments,
                    onPerfilClick = onNavigateToProfile
                )
            },
            containerColor = AppTheme.colors.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Search Bar
                SearchSection(
                    query = state.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChanged
                )

                // Specialties Filter
                SpecialtiesSection(
                    specialties = state.specialties,
                    selectedSpecialty = state.selectedSpecialty,
                    onSpecialtySelect = viewModel::onSpecialtySelected
                )

                // Clinics List
                ClinicsSection(
                    clinics = state.filteredClinics,
                    onClinicClick = onNavigateToClinicDetail
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun SearchSection(query: String, onQueryChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            placeholder = { Text("Buscar clínicas o especialistas...", color = AppTheme.colors.textSecondary) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = AppTheme.colors.textSecondary
                )
            },
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = AppTheme.colors.surface,
                unfocusedContainerColor = AppTheme.colors.surface,
                disabledContainerColor = AppTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = AppTheme.colors.textPrimary,
                unfocusedTextColor = AppTheme.colors.textPrimary
            )
        )
    }
}

@Composable
fun SpecialtiesSection(
    specialties: List<String>,
    selectedSpecialty: String?,
    onSpecialtySelect: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
        Text(
            "Especialidades",
            modifier = Modifier.padding(horizontal = 16.dp),
            style = AppTheme.typography.headlineLarge.copy(
                fontSize = 18.sp,
                color = AppTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(specialties) { specialty ->
                val isSelected = specialty == selectedSpecialty
                SpecialtyItem(
                    name = specialty,
                    isSelected = isSelected,
                    onClick = { onSpecialtySelect(specialty) }
                )
            }
        }
    }
}

@Composable
fun SpecialtyItem(name: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (isSelected) AppTheme.colors.primary.copy(alpha = 0.2f) else AppTheme.colors.surface)
                .border(
                    width = 1.dp,
                    color = if (isSelected) AppTheme.colors.primary.copy(alpha = 0.5f) else Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = when(name.lowercase()) {
                    "cardiología", "cardio" -> Icons.Default.Favorite
                    "pediatría" -> Icons.Default.ChildCare
                    "dermatología", "derma" -> Icons.Default.Face
                    "neurología", "neuro" -> Icons.Default.Psychology
                    "oftalmología", "oftalmo" -> Icons.Default.Visibility
                    else -> Icons.Default.MedicalServices
                },
                contentDescription = name,
                tint = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            name,
            style = AppTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) AppTheme.colors.textPrimary else AppTheme.colors.textSecondary
            )
        )
    }
}

@Composable
fun ClinicsSection(clinics: List<Clinic>, onClinicClick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Clínicas Disponibles",
                style = AppTheme.typography.headlineLarge.copy(
                    fontSize = 18.sp,
                    color = AppTheme.colors.textPrimary,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                "Ver Todas",
                style = AppTheme.typography.bodySmall.copy(
                    color = AppTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        clinics.forEach { clinic ->
            ClinicCard(clinic = clinic, onClick = { onClinicClick(clinic.id) })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ClinicCard(clinic: Clinic, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                AsyncImage(
                    model = clinic.imageUrl ?: "https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?q=80&w=1000&auto=format&fit=crop",
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (clinic.isOpen) {
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .align(Alignment.TopStart)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF10B981))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Abierto ahora",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                
                IconButton(
                    onClick = { /* TODO: Favorite */ },
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = Color.White
                    )
                }
            }
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    clinic.name,
                    style = AppTheme.typography.headlineLarge.copy(
                        fontSize = 18.sp,
                        color = AppTheme.colors.textPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = AppTheme.colors.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        clinic.address,
                        style = AppTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            color = AppTheme.colors.textSecondary
                        )
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    clinic.specialties.take(3).forEach { specialty ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AppTheme.colors.background.copy(alpha = 0.5f))
                                .border(1.dp, AppTheme.colors.textSecondary.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                specialty,
                                style = AppTheme.typography.bodySmall.copy(
                                    fontSize = 10.sp,
                                    color = AppTheme.colors.textSecondary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentScreen: String,
    onExplorarClick: () -> Unit,
    onCitasClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    Surface(
        color = AppTheme.colors.surface.copy(alpha = 0.95f),
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                icon = Icons.Default.Search,
                label = "Explorar",
                isSelected = currentScreen == "Explorar",
                onClick = onExplorarClick
            )
            NavItem(
                icon = Icons.Default.CalendarMonth,
                label = "Mis Citas",
                isSelected = currentScreen == "Mis Citas",
                onClick = onCitasClick
            )
            NavItem(
                icon = Icons.Default.Person,
                label = "Perfil",
                isSelected = currentScreen == "Perfil",
                onClick = onPerfilClick
            )
        }
    }
}

@Composable
fun NavItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
    val color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textSecondary
    val bgColor = if (isSelected) AppTheme.colors.primary.copy(alpha = 0.1f) else Color.Transparent
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Text(
            label,
            style = AppTheme.typography.bodySmall.copy(
                fontSize = 10.sp,
                color = color,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}
