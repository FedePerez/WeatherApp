package com.example.weatherapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.utils.LocationManager
import com.example.weatherapp.domain.models.WeatherInfo
import com.example.weatherapp.domain.usecases.GetWeatherByCityUseCase
import com.example.weatherapp.domain.usecases.GetWeatherByLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherByCityUseCase: GetWeatherByCityUseCase,
    private val getWeatherByLocationUseCase: GetWeatherByLocationUseCase,
    private val locationManager: LocationManager
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val weatherState: StateFlow<WeatherUiState> = _weatherState.asStateFlow()

    private val _cities = listOf("Montevideo", "Londres", "San Pablo", "Buenos Aires", "Munich")
    val cities: List<String> = _cities

    init {
        getWeatherByCity("Montevideo")
    }

    fun getWeatherByCity(cityName: String) {
        viewModelScope.launch {
            try {
                _weatherState.value = WeatherUiState.Loading

                getWeatherByCityUseCase(cityName).fold(
                    onSuccess = { weatherInfo ->
                        _weatherState.value = WeatherUiState.Success(weatherInfo)
                    },
                    onFailure = { error ->
                        _weatherState.value = WeatherUiState.Error(
                            error.message ?: "Error desconocido al obtener clima para $cityName"
                        )
                    }
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _weatherState.value = WeatherUiState.Error(
                    "Error inesperado al obtener clima: ${e.message}"
                )
            }
        }
    }

    fun getWeatherByCurrentLocation() {
        viewModelScope.launch {
            try {
                _weatherState.value = WeatherUiState.Loading

                locationManager.getCurrentLocation().fold(
                    onSuccess = { location ->
                        try {
                            getWeatherByLocationUseCase(location.latitude, location.longitude).fold(
                                onSuccess = { weatherInfo ->
                                    _weatherState.value = WeatherUiState.Success(weatherInfo)
                                },
                                onFailure = { error ->
                                    _weatherState.value = WeatherUiState.Error(
                                        "No se pudo obtener el clima para tu ubicación: ${error.message}"
                                    )
                                    // Si falla la obtención del clima, volver a una ciudad predeterminada
                                    getWeatherByCity("Montevideo")
                                }
                            )
                        } catch (e: Exception) {
                            _weatherState.value = WeatherUiState.Error(
                                "Error al procesar el clima para tu ubicación: ${e.message}"
                            )
                            getWeatherByCity("Montevideo")
                        }
                    },
                    onFailure = { error ->
                        _weatherState.value = WeatherUiState.Error(
                            "No se pudo obtener tu ubicación actual: ${error.message}"
                        )
                        // Si falla la ubicación, volver a una ciudad predeterminada
                        getWeatherByCity("Montevideo")
                    }
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _weatherState.value = WeatherUiState.Error(
                    "Error inesperado al obtener ubicación: ${e.message}"
                )
                // Si hay una excepción no controlada, volver a una ciudad predeterminada
                getWeatherByCity("Montevideo")
            }
        }
    }
}

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weatherInfo: WeatherInfo) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}