package com.gestorplus.appgestor.profile.presentation.state

sealed interface ProfileEfffect {
    data class ShowSnackbar(val message: String) : ProfileEfffect
    data object NavigateNext : ProfileEfffect
}
