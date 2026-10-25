package com.nabil.smartkrishi.data.local.converters

import androidx.room.TypeConverter
import com.nabil.smartkrishi.data.local.entity.DailyForecastEntity
import com.nabil.smartkrishi.data.local.entity.HourlyForecastEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Room TypeConverters to handle list serialization for Hourly and Daily weather forecasts.
 */
class WeatherConverters {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromHourlyForecastList(value: List<HourlyForecastEntity>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toHourlyForecastList(value: String): List<HourlyForecastEntity> {
        return if (value.isBlank()) emptyList() else json.decodeFromString(value)
    }

    @TypeConverter
    fun fromDailyForecastList(value: List<DailyForecastEntity>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toDailyForecastList(value: String): List<DailyForecastEntity> {
        return if (value.isBlank()) emptyList() else json.decodeFromString(value)
    }
}
