package com.example.weatherapp.data.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class LocationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices
        .getFusedLocationProviderClient(context)

    suspend fun getCurrentLocation(): Result<Location> {
        if (!hasLocationPermissions()) {
            return Result.failure(Exception("Permisos de ubicación no otorgados"))
        }

        if (!isLocationEnabled()) {
            return Result.failure(Exception("Servicios de ubicación desactivados"))
        }

        val lastLocationResult = getLastLocation()

        if (lastLocationResult.isSuccess) {
            return lastLocationResult
        }

        return requestNewLocation()
    }

    private fun hasLocationPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
    }

    private suspend fun getLastLocation(): Result<Location> = suspendCancellableCoroutine { continuation ->
        try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                continuation.resume(Result.failure(Exception("Permisos de ubicación no otorgados")))
                return@suspendCancellableCoroutine
            }

            try {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            continuation.resume(Result.success(location))
                        } else {
                            continuation.resume(Result.failure(Exception("Última ubicación no disponible")))
                        }
                    }
                    .addOnFailureListener { e ->
                        continuation.resume(Result.failure(e))
                    }
            } catch (securityException: SecurityException) {
                continuation.resume(Result.failure(Exception("Error de permisos de ubicación: ${securityException.message}")))
            }
        } catch (e: Exception) {
            continuation.resume(Result.failure(e))
        }
    }

    private suspend fun requestNewLocation(): Result<Location> = suspendCancellableCoroutine { continuation ->
        try {
            val locationRequest = LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = 0
                numUpdates = 1
            }

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    fusedLocationClient.removeLocationUpdates(this)

                    val location = locationResult.lastLocation
                    if (location != null) {
                        continuation.resume(Result.success(location))
                    } else {
                        continuation.resume(Result.failure(Exception("No se pudo obtener la ubicación")))
                    }
                }

                override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                    if (!locationAvailability.isLocationAvailable) {
                        fusedLocationClient.removeLocationUpdates(this)
                        continuation.resume(Result.failure(Exception("Ubicación no disponible")))
                    }
                }
            }

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                continuation.resume(Result.failure(Exception("Permisos de ubicación no otorgados")))
                return@suspendCancellableCoroutine
            }

            try {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } catch (securityException: SecurityException) {
                continuation.resume(Result.failure(Exception("Error de permisos de ubicación: ${securityException.message}")))
                return@suspendCancellableCoroutine
            }

            continuation.invokeOnCancellation {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        } catch (e: Exception) {
            continuation.resume(Result.failure(e))
        }
    }
}