package com.example.weatherapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.domain.models.WeatherInfo
import com.example.weatherapp.presentation.components.CityName
import com.example.weatherapp.presentation.components.CitySelector
import com.example.weatherapp.presentation.components.TemperatureItem
import com.example.weatherapp.presentation.components.WeatherDescription
import com.example.weatherapp.presentation.components.WeatherIcon
import com.example.weatherapp.presentation.components.WindInfo
import com.example.weatherapp.presentation.viewmodels.WeatherUiState
import com.example.weatherapp.presentation.viewmodels.WeatherViewModel

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    onRequestPermission: () -> Unit
) {
    val weatherState by viewModel.weatherState.collectAsState()
    val cities = viewModel.cities
    val scrollState = rememberScrollState()

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {
                val width = maxWidth
                val height = maxHeight
                val isWideLayout = width > height

                if (isWideLayout) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(0.4f)
                                .fillMaxHeight()
                                .padding(end = 8.dp)
                                .verticalScroll(scrollState)
                        ) {
                            ControlPanel(
                                cities = cities,
                                onCitySelected = { viewModel.getWeatherByCity(it) },
                                onRequestPermission = onRequestPermission
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(0.6f)
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState())
                        ) {
                            WeatherStateContent(weatherState = weatherState)
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(scrollState)
                    ) {
                        ControlPanel(
                            cities = cities,
                            onCitySelected = { viewModel.getWeatherByCity(it) },
                            onRequestPermission = onRequestPermission
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        WeatherStateContent(weatherState = weatherState)
                    }
                }
            }
        }
    }
}

@Composable
private fun ControlPanel(
    cities: List<String>,
    onCitySelected: (String) -> Unit,
    onRequestPermission: () -> Unit
) {
    CitySelector(
        cities = cities,
        onCitySelected = onCitySelected
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = onRequestPermission,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Ubicación Actual"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Usar Mi Ubicación")
    }
}

@Composable
private fun WeatherStateContent(weatherState: WeatherUiState) {
    when (weatherState) {
        is WeatherUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is WeatherUiState.Success -> {
            WeatherContent(weatherInfo = weatherState.weatherInfo)
        }
        is WeatherUiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = weatherState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun WeatherContent(weatherInfo: WeatherInfo) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val isWideEnough = maxWidth > 600.dp

        if (isWideEnough) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    CityName(name = weatherInfo.cityName)
                    Spacer(modifier = Modifier.height(16.dp))
                    WeatherIcon(iconCode = weatherInfo.weatherIcon)
                    Spacer(modifier = Modifier.height(8.dp))
                    WeatherDescription(description = weatherInfo.weatherDescription)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    WeatherTemperatureCard(weatherInfo = weatherInfo)
                    Spacer(modifier = Modifier.height(16.dp))
                    WindInfo(
                        speed = weatherInfo.windSpeed,
                        direction = weatherInfo.windDirection,
                        humidity = weatherInfo.humidity,
                    )
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                CityName(name = weatherInfo.cityName)
                Spacer(modifier = Modifier.height(16.dp))
                WeatherIcon(iconCode = weatherInfo.weatherIcon)
                Spacer(modifier = Modifier.height(8.dp))
                WeatherDescription(description = weatherInfo.weatherDescription)
                Spacer(modifier = Modifier.height(16.dp))
                WeatherTemperatureCard(weatherInfo = weatherInfo)
                Spacer(modifier = Modifier.height(16.dp))
                WindInfo(
                    speed = weatherInfo.windSpeed,
                    direction = weatherInfo.windDirection,
                    humidity = weatherInfo.humidity,
                )
            }
        }
    }
}

@Composable
fun WeatherTemperatureCard(weatherInfo: WeatherInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Temperatura",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            TemperatureItem(
                label = "Actual",
                temperature = weatherInfo.currentTemp,
                icon = Icons.Default.Thermostat
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TemperatureItem(
                    label = "Mínima",
                    temperature = weatherInfo.minTemp,
                    icon = Icons.Default.KeyboardArrowDown
                )

                TemperatureItem(
                    label = "Máxima",
                    temperature = weatherInfo.maxTemp,
                    icon = Icons.Default.KeyboardArrowUp
                )
            }
        }
    }
}