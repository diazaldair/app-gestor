package com.gestorplus.appgestor.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MapView(
    modifier: Modifier,
    latitude: Double,
    longitude: Double,
    onLocationSelected: (lat: Double, lng: Double, address: String?, department: String?) -> Unit
)
