package com.gestorplus.appgestor.onboarding.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestorplus.appgestor.core.locale.getDeviceLanguage
import com.gestorplus.appgestor.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.gestorplus.appgestor.onboarding.domain.usecase.GetOnboardingConfigUseCase
import com.gestorplus.appgestor.onboarding.presentation.state.OnboardingEffect
import com.gestorplus.appgestor.onboarding.presentation.state.OnboardingEvent
import com.gestorplus.appgestor.onboarding.presentation.state.OnboardingUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val getOnboardingConfigUseCase: GetOnboardingConfigUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<OnboardingEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadConfig()
    }

    fun resetToFirstSlide() {
        _state.update { it.copy(currentIndex = 0) }
    }

    private fun loadConfig() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, language = getDeviceLanguage()) }
            try {
                val slides = getOnboardingConfigUseCase()
                _state.update { it.copy(isLoading = false, slides = slides) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun onEvent(event: OnboardingEvent) {
        viewModelScope.launch {
            when (event) {
                OnboardingEvent.OnNextClicked -> {
                    if (!_state.value.isLastSlide) {
                        _state.update { it.copy(currentIndex = it.currentIndex + 1) }
                    }
                }
                OnboardingEvent.OnPreviousClicked -> {
                    if (!_state.value.isFirstSlide) {
                        _state.update { it.copy(currentIndex = it.currentIndex - 1) }
                    }
                }
                OnboardingEvent.OnSkipClicked -> {
                    _effect.emit(OnboardingEffect.NavigateToHome)
                }
                OnboardingEvent.OnStartClicked -> {
                    completeOnboardingUseCase()
                    _effect.emit(OnboardingEffect.NavigateToHome)
                }
            }
        }
    }
}
