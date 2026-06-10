package com.gestorplus.appgestor.core.presentation.components

import android.location.Address
import android.location.Geocoder
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView as OsmMapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.Locale

@Composable
actual fun MapView(
    modifier: Modifier,
    latitude: Double,
    longitude: Double,
    onLocationSelected: (lat: Double, lng: Double, address: String?, department: String?) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val geocoder = remember { Geocoder(context, Locale.getDefault()) }
    
    Configuration.getInstance().userAgentValue = context.packageName

    val mapView = remember {
        OsmMapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            controller.setZoom(16.5)
        }
    }

    val myLocationOverlay = remember {
        MyLocationNewOverlay(GpsMyLocationProvider(context), mapView).apply {
            enableMyLocation()
        }
    }

    val marker = remember {
        Marker(mapView).apply {
            isDraggable = true
            setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
                override fun onMarkerDrag(marker: Marker?) {}
                override fun onMarkerDragEnd(marker: Marker?) {
                    marker?.position?.let { p ->
                        scope.launch {
                            val addressInfo = getAddressInfoFromCoords(geocoder, p.latitude, p.longitude)
                            onLocationSelected(p.latitude, p.longitude, addressInfo.first, addressInfo.second)
                        }
                    }
                }
                override fun onMarkerDragStart(marker: Marker?) {}
            })
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                val startPoint = GeoPoint(latitude, longitude)
                mapView.controller.setCenter(startPoint)
                
                marker.position = startPoint
                mapView.overlays.add(marker)
                mapView.overlays.add(myLocationOverlay)

                val eventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
                    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                        p?.let {
                            marker.position = it
                            mapView.invalidate()
                            scope.launch {
                                val addressInfo = getAddressInfoFromCoords(geocoder, it.latitude, it.longitude)
                                onLocationSelected(it.latitude, it.longitude, addressInfo.first, addressInfo.second)
                            }
                        }
                        return true
                    }
                    override fun longPressHelper(p: GeoPoint?): Boolean = false
                })
                mapView.overlays.add(eventsOverlay)
                
                mapView
            },
            update = {
                val p = GeoPoint(latitude, longitude)
                if (it.mapCenter.latitude != latitude || it.mapCenter.longitude != longitude) {
                    it.controller.animateTo(p)
                    marker.position = p
                    it.invalidate()
                }
            }
        )

        FloatingActionButton(
            onClick = {
                myLocationOverlay.myLocation?.let {
                    mapView.controller.animateTo(it)
                    marker.position = it
                    mapView.invalidate()
                    scope.launch {
                        val addressInfo = getAddressInfoFromCoords(geocoder, it.latitude, it.longitude)
                        onLocationSelected(it.latitude, it.longitude, addressInfo.first, addressInfo.second)
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(40.dp),
            shape = CircleShape,
            containerColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "Mi ubicación",
                tint = androidx.compose.ui.graphics.Color(0xFF3B82F6),
                modifier = Modifier.size(20.dp)
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            myLocationOverlay.disableMyLocation()
            mapView.onDetach()
        }
    }
}

private suspend fun getAddressInfoFromCoords(geocoder: Geocoder, lat: Double, lng: Double): Pair<String?, String?> = withContext(Dispatchers.IO) {
    try {
        @Suppress("DEPRECATION")
        val addresses = geocoder.getFromLocation(lat, lng, 1)
        if (!addresses.isNullOrEmpty()) {
            val addr = addresses[0]
            val street = addr.thoroughfare ?: ""
            val number = addr.subThoroughfare ?: ""
            
            val formattedAddress = if (street.isNotEmpty()) {
                if (number.isNotEmpty()) "$street #$number" else street
            } else {
                addr.getAddressLine(0)?.split(",")?.firstOrNull()
            }
            
            val department = addr.adminArea // En Bolivia suele devolver el departamento
            
            Pair(formattedAddress, department)
        } else Pair(null, null)
    } catch (e: Exception) {
        Pair(null, null)
    }
}
