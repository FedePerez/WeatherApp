package com.example.weatherapp.domain.usecases

import com.example.weatherapp.domain.models.WeatherInfo
import com.example.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherByLocationUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): Result<WeatherInfo> {
        return weatherRepository.getWeatherByLocation(latitude, longitude)
    }
}