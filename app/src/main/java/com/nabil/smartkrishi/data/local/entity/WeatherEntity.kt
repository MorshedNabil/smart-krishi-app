package com.nabil.smartkrishi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Room Database Entity for caching Weather data locally.
 */
@Entity(tableName = "weather_data")
data class WeatherEntity(
    @PrimaryKey
    val location: String = "default",

    // Current Weather Details
    val time: String,
    val temperature2m: Double,
    val relativeHumidity2m: Double,
    val isDay: Boolean,
    val rain: Double,
    val precipitation: Double,
    val windSpeed10m: Double,

    // Hourly and Daily Forecasts (Stored as JSON via Room TypeConverters)
    val hourlyForecasts: List<HourlyForecastEntity>,
    val dailyForecasts: List<DailyForecastEntity>,

    // Cache metadata
    val lastUpdated: Long = System.currentTimeMillis()
)

@Serializable
data class HourlyForecastEntity(
    val date: String,
    val temperature2m: Double,
    val rain: Double,
    val relativeHumidity2m: Double,
    val windSpeed10m: Double,
    val soilTemperature0cm: Double
)

@Serializable
data class DailyForecastEntity(
    val date: String,
    val sunrise: String,
    val sunset: String,
    val showersSum: Double,
    val temperature2mMax: Double,
    val temperature2mMin: Double,
    val uvIndexMax: Double
)
