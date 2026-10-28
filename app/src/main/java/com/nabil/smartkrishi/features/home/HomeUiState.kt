package com.nabil.smartkrishi.features.home

import com.nabil.smartkrishi.domain.model.MarketPrice
import com.nabil.smartkrishi.domain.model.Weather

// UI State representing different network states for Home Screen
sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val marketPrice: MarketPrice) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

// UI State for Date in Header Section
data class DateUiState(
    val date: String = ""
)

// UI State for weather
data class WeatherState(
    val weather: Weather? = null
)