package com.example.weatherapp.data.repository

import android.content.Context
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.api.WeatherApiService
import com.example.weatherapp.data.models.WeatherResponse
import com.example.weatherapp.domain.models.WeatherInfo
import com.example.weatherapp.domain.repository.WeatherRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService,
    @ApplicationContext private val context: Context
) : WeatherRepository {

    private val apiKey = BuildConfig.WEATHER_API_KEY

    override suspend fun getWeatherByCity(cityName: String): Result<WeatherInfo> {
        return try {
            println("apiKey: $apiKey")
            val response = weatherApiService.getWeatherByCity(
                cityName = getEnglishCityName(cityName),
                apiKey = apiKey
            )
            println("response: $response")
            Result.success(response.toWeatherInfo())
        } catch (e: Exception) {
            println("Error en el repositoryImpl")
            Result.failure(e)
        }
    }

    override suspend fun getWeatherByLocation(latitude: Double, longitude: Double): Result<WeatherInfo> {
        return try {
            val response = weatherApiService.getWeatherByLocation(
                latitude = latitude,
                longitude = longitude,
                apiKey = apiKey
            )
            Result.success(response.toWeatherInfo())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getEnglishCityName(cityName: String): String {
        return when (cityName) {
            "Londres" -> "London"
            "San Pablo" -> "Sao Paulo"
            "Munich" -> "Munich"
            else -> cityName
        }
    }

    private fun WeatherResponse.toWeatherInfo(): WeatherInfo {
        return WeatherInfo(
            cityName = name,
            currentTemp = main.temp,
            minTemp = main.temp_min,
            maxTemp = main.temp_max,
            windSpeed = wind.speed,
            windDirection = wind.deg,
            weatherIcon = weather.firstOrNull()?.icon ?: "",
            weatherDescription = weather.firstOrNull()?.description ?: "",
            humidity = main.humidity
        )
    }
}