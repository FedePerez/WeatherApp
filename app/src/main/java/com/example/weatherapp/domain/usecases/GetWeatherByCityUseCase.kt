package com.example.weatherapp.domain.usecases

import com.example.weatherapp.domain.models.WeatherInfo
import com.example.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherByCityUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(cityName: String): Result<WeatherInfo> {
        return weatherRepository.getWeatherByCity(cityName)
    }
}