package com.nabil.smartkrishi.domain.model

/**
 * Domain Object (DO) representing weather data used in the UI layer.
 */
data class Weather(
    val location: String,
    val time: String,
    val temperature: Double,
    val formattedTemperature: String,
    val relativeHumidity: Double,
    val isDay: Boolean,
    val rain: Double,
    val precipitation: Double,
    val windSpeed: Double,
    val hourlyForecasts: List<HourlyForecast>,
    val dailyForecasts: List<DailyForecast>,
    val lastUpdatedFormatted: String
)

data class HourlyForecast(
    val time: String,
    val temperature: Double,
    val formattedTemperature: String,
    val rain: Double,
    val relativeHumidity: Double,
    val windSpeed: Double,
    val soilTemperature: Double
)

data class DailyForecast(
    val date: String,
    val sunrise: String,
    val sunset: String,
    val showersSum: Double,
    val maxTemp: Double,
    val minTemp: Double,
    val formattedTempRange: String,
    val maxUvIndex: Double
)
