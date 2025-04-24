package com.example.weatherapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun WeatherIcon(iconCode: String) {
    val iconUrl = "https://openweathermap.org/img/wn/$iconCode@2x.png"
    AsyncImage(
        model = iconUrl,
        contentDescription = "Weather Icon",
        modifier = Modifier
            .size(100.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = CircleShape
            )
            .padding(8.dp)
    )
}