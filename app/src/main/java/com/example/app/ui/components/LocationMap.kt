package com.example.app.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.core.content.ContextCompat
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Componente reutilizable del mapa con ubicación del usuario
 * 
 * @param modifier Modificador para el contenedor del mapa
 * @param initialLocation Ubicación inicial del mapa (opcional). Si es null, se intentará obtener la ubicación del usuario
 * @param onLocationObtained Callback cuando se obtiene la ubicación del usuario
 * @param onMapReady Callback cuando el mapa está listo
 */
@Composable
fun LocationMap(
    modifier: Modifier = Modifier,
    initialLocation: Point? = null,
    onLocationObtained: (Point?) -> Unit = {},
    onMapReady: (() -> Unit)? = null
) {
    val context = LocalContext.current
    var userLocation by remember { mutableStateOf<Point?>(null) }
    
    // Función para verificar permisos
    fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    // Obtener ubicación del usuario cuando se otorguen permisos
    LaunchedEffect(Unit) {
        if (checkLocationPermission() && userLocation == null) {
            val location = withContext(Dispatchers.IO) {
                getCurrentLocation(context)
            }
            if (location != null) {
                userLocation = location
                onLocationObtained(location)
            }
        }
    }
    
    // Usar ubicación inicial si se proporciona, sino usar ubicación del usuario o ubicación por defecto
    val mapCenter = remember(initialLocation, userLocation) {
        initialLocation ?: userLocation ?: Point.fromLngLat(-98.0, 39.5)
    }
    
    val mapViewportState = rememberMapViewportState {
        if (userLocation != null || initialLocation != null) {
            CameraOptions.Builder()
                .zoom(15.0)
                .center(mapCenter)
                .pitch(45.0)
                .bearing(0.0)
                .build()
        } else {
            CameraOptions.Builder()
                .zoom(2.0)
                .center(Point.fromLngLat(-98.0, 39.5))
                .pitch(0.0)
                .bearing(0.0)
                .build()
        }
    }
    
    MapboxMap(
        modifier = modifier.clip(RoundedCornerShape(12.dp)),
        mapViewportState = mapViewportState
    ) {
        // Usar MapEffect para actualizar la cámara cuando se obtenga la ubicación
        MapEffect(userLocation ?: initialLocation) { mapView ->
            val locationToUse = userLocation ?: initialLocation
            if (locationToUse != null) {
                val cameraOptions = CameraOptions.Builder()
                    .zoom(15.0)
                    .center(locationToUse)
                    .pitch(45.0)
                    .bearing(0.0)
                    .build()
                mapView.getMapboxMap().setCamera(cameraOptions)
                onMapReady?.invoke()
            }
        }
        
        // Aquí se pueden agregar marcadores en el futuro
    }
}


private suspend fun getCurrentLocation(context: Context): Point? {
    return withContext(Dispatchers.IO) {
        try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            
            if (!isGpsEnabled && !isNetworkEnabled) {
                return@withContext null
            }
            
            var location: Location? = null
            
            if (isGpsEnabled && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
            
            if (location == null && isNetworkEnabled && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
            
            if (location == null) {
                val locationResult = kotlinx.coroutines.CompletableDeferred<Location?>()
                
                val locationListener = object : LocationListener {
                    override fun onLocationChanged(loc: Location) {
                        locationResult.complete(loc)
                        try {
                            if (isGpsEnabled && ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                locationManager.removeUpdates(this)
                            } else if (isNetworkEnabled && ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                locationManager.removeUpdates(this)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    
                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                }
                
                try {
                    if (isGpsEnabled && ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0L,
                            0f,
                            locationListener
                        )
                    } else if (isNetworkEnabled && ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            0L,
                            0f,
                            locationListener
                        )
                    }
                    
                    location = withTimeoutOrNull(5000) {
                        locationResult.await()
                    }
                    
                    try {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            locationManager.removeUpdates(locationListener)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    try {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            locationManager.removeUpdates(locationListener)
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }
            
            location?.let {
                Point.fromLngLat(it.longitude, it.latitude)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

