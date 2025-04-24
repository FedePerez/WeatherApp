package com.example.weatherapp.domain.repository

import com.example.weatherapp.domain.models.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherByCity(cityName: String): Result<WeatherInfo>
    suspend fun getWeatherByLocation(latitude: Double, longitude: Double): Result<WeatherInfo>
}