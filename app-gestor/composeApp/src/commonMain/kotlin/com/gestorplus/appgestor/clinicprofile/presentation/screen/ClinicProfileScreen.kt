package com.gestorplus.appgestor.clinicprofile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.clinicprofile.presentation.composable.ClinicServiceCard
import com.gestorplus.appgestor.clinicprofile.presentation.state.ClinicProfileEvent
import com.gestorplus.appgestor.clinicprofile.presentation.viewmodel.ClinicProfileViewModel
import com.gestorplus.appgestor.designsystem.theme.AppTheme
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import org.jetbrains.compose.resources.stringResource
import app_gestor.composeapp.generated.resources.*
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicProfileScreen(
    viewModel: ClinicProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    DsTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(Res.string.clinic_profile_title),
                            style = AppTheme.typography.headlineLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.onEvent(ClinicProfileEvent.OnBackClicked) }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(Res.string.common_back),
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Action */ }) {
                            Icon(
                                imageVector = Icons.Outlined.AddBox,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = AppTheme.colors.background
                    )
                )
            },
            bottomBar = {
                ClinicBottomNavigationBar()
            },
            containerColor = AppTheme.colors.background
        ) { paddingValues ->
            state.clinicProfile?.let { profile ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header Image Area
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(16.dp)
                            .clip(RoundedCornerShape(24.dp))
                    ) {
                        // Image Placeholder
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFF1E293B))
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.MedicalServices,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp).align(Alignment.Center),
                                tint = AppTheme.colors.primary.copy(alpha = 0.2f)
                            )
                        }

                        // Gradient Overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                        startY = 400f
                                    )
                                )
                        )

                        // Title and Subtitle Overlay
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(20.dp)
                        ) {
                            Text(
                                text = profile.name,
                                color = Color.White,
                                style = AppTheme.typography.headlineLarge.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = profile.subtitle,
                                color = Color(0xFF94A3B8),
                                style = AppTheme.typography.bodyMedium.copy(fontSize = 14.sp)
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Spacer(modifier = Modifier.height(8.dp))

                        // Bio Section
                        Text(
                            text = stringResource(Res.string.clinic_profile_bio_title),
                            style = AppTheme.typography.headlineLarge.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = profile.bio,
                            style = AppTheme.typography.bodyMedium.copy(lineHeight = 22.sp, fontSize = 15.sp),
                            color = Color(0xFF94A3B8)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Specialties Section
                        Text(
                            text = stringResource(Res.string.clinic_profile_specialties_title),
                            style = AppTheme.typography.headlineLarge.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        SpecialtiesFlowRow(profile.specialties)

                        Spacer(modifier = Modifier.height(24.dp))

                        // Location Section
                        Text(
                            text = stringResource(Res.string.clinic_profile_location_title),
                            style = AppTheme.typography.headlineLarge.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = AppTheme.colors.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = profile.location,
                                    style = AppTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, fontSize = 15.sp),
                                    color = Color.White
                                )
                                Text(
                                    text = stringResource(Res.string.clinic_profile_view_map),
                                    style = AppTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = AppTheme.colors.primary,
                                    modifier = Modifier.clickable { /* Map Action */ }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Services Section
                        Text(
                            text = stringResource(Res.string.clinic_profile_services_title),
                            style = AppTheme.typography.headlineLarge.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        profile.services.forEach { service ->
                            ClinicServiceCard(
                                service = service,
                                onReserveClick = { viewModel.onEvent(ClinicProfileEvent.OnReserveService(it)) }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppTheme.colors.primary)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SpecialtiesFlowRow(specialties: List<String>) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        specialties.forEach { specialty ->
            Surface(
                color = Color.Transparent,
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Text(
                    text = specialty,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = AppTheme.typography.labelLarge.copy(fontSize = 14.sp),
                    color = Color(0xFF94A3B8)
                )
            }
        }
    }
}

@Composable
fun ClinicBottomNavigationBar() {
    Surface(
        color = AppTheme.colors.background,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            NavItem(Icons.Default.Search, stringResource(Res.string.nav_find), false)
            NavItem(Icons.Default.DateRange, stringResource(Res.string.nav_bookings), false)
            NavItem(Icons.Default.Person, stringResource(Res.string.nav_profile), true)
        }
    }
}

@Composable
private fun NavItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean) {
    val color = if (isSelected) Color.White else Color.Gray
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { /* Nav Action */ }) {
        Icon(icon, label, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = color, style = AppTheme.typography.labelSmall)
    }
}
