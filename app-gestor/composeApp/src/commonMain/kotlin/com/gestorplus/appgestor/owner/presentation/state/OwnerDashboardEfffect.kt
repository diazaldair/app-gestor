package com.gestorplus.appgestor.owner.presentation.state

sealed interface OwnerDashboardEfffect {
    data class ShowSnackbar(val message: String) : OwnerDashboardEfffect
}
