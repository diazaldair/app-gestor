package com.gestorplus.appgestor.onboarding.presentation.state

import androidx.compose.runtime.Immutable
import com.gestorplus.appgestor.onboarding.domain.model.OnboardingSlide

@Immutable
data class OnboardingUiState(
    val slides: List<OnboardingSlide> = emptyList(),
    val currentIndex: Int = 0,
    val isLoading: Boolean = true,
    val language: String = "en",
    val errorMessage: String? = null
) {
    val isFirstSlide: Boolean get() = currentIndex == 0
    val isLastSlide: Boolean get() = currentIndex == slides.lastIndex && slides.isNotEmpty()
}
