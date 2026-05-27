package com.gestorplus.appgestor.onboarding.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.gestorplus.appgestor.designsystem.theme.DsTheme
import com.gestorplus.appgestor.onboarding.domain.model.OnboardingSlide
import com.gestorplus.appgestor.onboarding.presentation.state.OnboardingEffect
import com.gestorplus.appgestor.onboarding.presentation.state.OnboardingEvent
import com.gestorplus.appgestor.onboarding.presentation.viewmodel.OnboardingViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

private val LightBgStart = Color(0xFFEBF8FF)
private val LightBgEnd = Color(0xFFFFFFFF)
private val PrimaryBlue = Color(0xFF3B82F6)
private val TextDark = Color(0xFF1E293B)
private val TextGray = Color(0xFF64748B)

@Composable
fun OnboardingScreen(
    onNavigateToHome: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                OnboardingEffect.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    DsTheme {
        Scaffold(
            containerColor = Color.Transparent
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(LightBgStart, LightBgEnd)
                        )
                    )
                    .padding(paddingValues)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = PrimaryBlue
                    )
                } else if (state.slides.isEmpty()) {
                    Text(
                        text = state.errorMessage ?: "No slides available",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    val currentSlide = state.slides[state.currentIndex]
                    
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Top Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!state.isFirstSlide) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .clickable { viewModel.onEvent(OnboardingEvent.OnPreviousClicked) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Previous",
                                        tint = TextDark
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.size(40.dp))
                            }

                            Box(
                                modifier = Modifier
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White)
                                    .clickable { viewModel.onEvent(OnboardingEvent.OnSkipClicked) }
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (state.language == "es") "Omitir" else if (state.language == "fr") "Passer" else "Skip",
                                    color = TextGray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Image
                        AsyncImage(
                            model = currentSlide.imageUrl[state.language] ?: currentSlide.imageUrl.values.firstOrNull(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .padding(horizontal = 16.dp),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        // Text Content
                        Text(
                            text = currentSlide.title[state.language] ?: currentSlide.title.values.firstOrNull() ?: "",
                            color = TextDark,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = currentSlide.description[state.language] ?: currentSlide.description.values.firstOrNull() ?: "",
                            color = TextGray,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        // Indicators
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            state.slides.forEachIndexed { index, _ ->
                                Box(
                                    modifier = Modifier
                                        .height(6.dp)
                                        .width(if (index == state.currentIndex) 24.dp else 6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(if (index == state.currentIndex) PrimaryBlue else Color(0xFFCBD5E1))
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Action Button
                        Button(
                            onClick = {
                                if (state.isLastSlide) {
                                    viewModel.onEvent(OnboardingEvent.OnStartClicked)
                                } else {
                                    viewModel.onEvent(OnboardingEvent.OnNextClicked)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                val buttonText = if (state.isLastSlide) {
                                    if (state.language == "es") "Iniciar" else if (state.language == "fr") "Commencer" else "Start"
                                } else {
                                    if (state.language == "es") "Siguiente" else if (state.language == "fr") "Suivant" else "Next"
                                }
                                
                                Text(
                                    text = buttonText,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
