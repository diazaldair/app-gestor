package com.gestorplus.appgestor.owner.dashboard.presentation.state

sealed interface OwnerDashboardEfffect {
    data class ShowSnackbar(val message: String) : OwnerDashboardEfffect
}
