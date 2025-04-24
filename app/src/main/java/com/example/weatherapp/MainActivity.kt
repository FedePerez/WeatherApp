package com.example.weatherapp

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.presentation.screens.WeatherScreen
import com.example.weatherapp.presentation.theme.WeatherAppTheme
import com.example.weatherapp.presentation.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val locationGranted = permissions.entries.all { it.value }

        if (locationGranted) {
            viewModel?.getWeatherByCurrentLocation()
        } else {
            Toast.makeText(
                this,
                "Se requiere permiso de ubicación para obtener el clima de su ubicación actual",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private var viewModel: WeatherViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            WeatherAppTheme {
                val weatherViewModel: WeatherViewModel = hiltViewModel()
                viewModel = weatherViewModel

                WeatherScreen(
                    viewModel = weatherViewModel,
                    onRequestPermission = { requestLocationPermission() }
                )
            }
        }
    }

    private fun requestLocationPermission() {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}