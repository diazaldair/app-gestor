package com.gestorplus.appgestor.presentation.profile

sealed interface ProfileEffect {
    data object NavigateNext : ProfileEffect
}