package com.nabil.smartkrishi.data.remote.dto

import com.nabil.smartkrishi.data.local.entity.DailyForecastEntity
import com.nabil.smartkrishi.data.local.entity.HourlyForecastEntity
import com.nabil.smartkrishi.data.local.entity.WeatherEntity
import com.nabil.smartkrishi.domain.model.DailyForecast
import com.nabil.smartkrishi.domain.model.HourlyForecast
import com.nabil.smartkrishi.domain.model.Weather
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Network Data Transfer Object (DTO) matching the Django Weather API response structure.
 */
@Serializable
data class WeatherResponseDto(
    @SerialName("current")
    val current: CurrentWeatherDto? = null,

    @SerialName("hourly")
    val hourly: List<HourlyWeatherDto>? = emptyList(),

    @SerialName("daily")
    val daily: List<DailyWeatherDto>? = emptyList()
)

@Serializable
data class CurrentWeatherDto(
    @SerialName("time")
    val time: String? = null,

    @SerialName("temperature_2m")
    val temperature2m: Double? = null,

    @SerialName("relative_humidity_2m")
    val relativeHumidity2m: Int? = null,

    @SerialName("is_day")
    val isDay: Int? = null,

    @SerialName("rain")
    val rain: Double? = null,

    @SerialName("precipitation")
    val precipitation: Double? = null,

    @SerialName("wind_speed_10m")
    val windSpeed10m: Double? = null
)

@Serializable
data class HourlyWeatherDto(
    @SerialName("date")
    val date: String? = null,

    @SerialName("temperature_2m")
    val temperature2m: Double? = null,

    @SerialName("rain")
    val rain: Double? = null,

    @SerialName("relative_humidity_2m")
    val relativeHumidity2m: Double? = null,

    @SerialName("wind_speed_10m")
    val windSpeed10m: Double? = null,

    @SerialName("soil_temperature_0cm")
    val soilTemperature0cm: Double? = null
)

@Serializable
data class DailyWeatherDto(
    @SerialName("date")
    val date: String? = null,

    @SerialName("sunrise")
    val sunrise: String? = null,

    @SerialName("sunset")
    val sunset: String? = null,

    @SerialName("showers_sum")
    val showersSum: Double? = null,

    @SerialName("temperature_2m_max")
    val temperature2mMax: Double? = null,

    @SerialName("temperature_2m_min")
    val temperature2mMin: Double? = null,

    @SerialName("uv_index_max")
    val uvIndexMax: Double? = null
)

// ============================================================================
// MAPPER FUNCTIONS: DTO -> Entity -> Domain Model
// ============================================================================

/**
 * Converts Network DTO to Room Database Entity for local caching.
 */
fun WeatherResponseDto.toEntity(location: String = "default"): WeatherEntity {
    val currentDto = current ?: CurrentWeatherDto()

    return WeatherEntity(
        location = location,
        time = currentDto.time ?: "",
        temperature2m = currentDto.temperature2m ?: 0.0,
        relativeHumidity2m = currentDto.relativeHumidity2m ?: 0,
        isDay = (currentDto.isDay == 1),
        rain = currentDto.rain ?: 0.0,
        precipitation = currentDto.precipitation ?: 0.0,
        windSpeed10m = currentDto.windSpeed10m ?: 0.0,
        hourlyForecasts = hourly?.map { it.toEntity() } ?: emptyList(),
        dailyForecasts = daily?.map { it.toEntity() } ?: emptyList(),
        lastUpdated = System.currentTimeMillis()
    )
}

fun HourlyWeatherDto.toEntity(): HourlyForecastEntity {
    return HourlyForecastEntity(
        date = date ?: "",
        temperature2m = temperature2m ?: 0.0,
        rain = rain ?: 0.0,
        relativeHumidity2m = relativeHumidity2m ?: 0.0,
        windSpeed10m = windSpeed10m ?: 0.0,
        soilTemperature0cm = soilTemperature0cm ?: 0.0
    )
}

fun DailyWeatherDto.toEntity(): DailyForecastEntity {
    return DailyForecastEntity(
        date = date ?: "",
        sunrise = sunrise ?: "",
        sunset = sunset ?: "",
        showersSum = showersSum ?: 0.0,
        temperature2mMax = temperature2mMax ?: 0.0,
        temperature2mMin = temperature2mMin ?: 0.0,
        uvIndexMax = uvIndexMax ?: 0.0
    )
}

/**
 * Converts Room Database Entity to Domain Object (DO) for UI presentation.
 */
fun WeatherEntity.toDomainModel(): Weather {
    return Weather(
        location = location,
        time = formatTime(time),
        temperature = temperature2m,
        formattedTemperature = "${temperature2m.toInt()}°C",
        relativeHumidity = relativeHumidity2m,
        isDay = isDay,
        rain = rain,
        precipitation = precipitation,
        windSpeed = windSpeed10m,
        hourlyForecasts = hourlyForecasts.map { it.toDomainModel() },
        dailyForecasts = dailyForecasts.map { it.toDomainModel() },
        lastUpdatedFormatted = formatLastUpdated(lastUpdated)
    )
}

fun HourlyForecastEntity.toDomainModel(): HourlyForecast {
    return HourlyForecast(
        time = formatTime(date),
        temperature = temperature2m,
        formattedTemperature = "${temperature2m.toInt()}°C",
        rain = rain,
        relativeHumidity = relativeHumidity2m,
        windSpeed = windSpeed10m,
        soilTemperature = soilTemperature0cm
    )
}

fun DailyForecastEntity.toDomainModel(): DailyForecast {
    return DailyForecast(
        date = formatDate(date),
        sunrise = formatTime(sunrise),
        sunset = formatTime(sunset),
        showersSum = showersSum,
        maxTemp = temperature2mMax,
        minTemp = temperature2mMin,
        formattedTempRange = "${temperature2mMax.toInt()}° / ${temperature2mMin.toInt()}°",
        maxUvIndex = uvIndexMax
    )
}

// ============================================================================
// HELPER FORMATTING FUNCTIONS
// ============================================================================

private fun formatTime(isoString: String): String {
    if (isoString.isBlank()) return "--:--"
    return try {
        val instant = Instant.parse(isoString)
        val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
            .withZone(ZoneId.systemDefault())
        formatter.format(instant)
    } catch (e: Exception) {
        if (isoString.length >= 16) isoString.substring(11, 16) else isoString
    }
}

private fun formatDate(isoString: String): String {
    if (isoString.isBlank()) return "--"
    return try {
        val instant = Instant.parse(isoString)
        val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM", Locale.ENGLISH)
            .withZone(ZoneId.systemDefault())
        formatter.format(instant)
    } catch (e: Exception) {
        if (isoString.length >= 10) isoString.substring(0, 10) else isoString
    }
}

private fun formatLastUpdated(timestampMillis: Long): String {
    return try {
        val instant = Instant.ofEpochMilli(timestampMillis)
        val formatter = DateTimeFormatter.ofPattern("dd MMM, hh:mm a", Locale.ENGLISH)
            .withZone(ZoneId.systemDefault())
        formatter.format(instant)
    } catch (e: Exception) {
        "Just now"
    }
}
