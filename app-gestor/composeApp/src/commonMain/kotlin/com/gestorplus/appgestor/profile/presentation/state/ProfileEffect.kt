package com.gestorplus.appgestor.profile.presentation.state

sealed interface ProfileEffect {
    data object NavigateNext : ProfileEffect
}
