package com.gestorplus.appgestor.profile.presentation.state

sealed interface ProfileEffect {
    data class ShowSnackbar(val message: String) : ProfileEffect
    data object NavigateNext : ProfileEffect
}
