package com.example.weatherapp.domain.models

data class WeatherInfo(
    val cityName: String,
    val currentTemp: Double,
    val minTemp: Double,
    val maxTemp: Double,
    val windSpeed: Double,
    val windDirection: Int,
    val weatherIcon: String,
    val weatherDescription: String,
    val humidity: Int
)