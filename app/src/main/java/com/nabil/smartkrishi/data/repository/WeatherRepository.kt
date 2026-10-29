package com.nabil.smartkrishi.data.repository

import android.util.Log
import com.nabil.smartkrishi.data.local.dao.WeatherDao
import com.nabil.smartkrishi.data.remote.ApiService
import com.nabil.smartkrishi.data.remote.RetrofitClient
import com.nabil.smartkrishi.data.remote.dto.toDomainModel
import com.nabil.smartkrishi.data.remote.dto.toEntity
import com.nabil.smartkrishi.domain.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository acting as Single Source of Truth (SSOT) for Weather data.
 *
 * Pattern:
 * 1. UI observes [getWeatherStream] which streams cached weather directly from Room.
 * 2. ViewModel triggers [refreshWeather] which fetches updated data from Django
 *    and inserts it into Room.
 * 3. Room automatically emits the updated data through [getWeatherStream] to the UI.
 */
class WeatherRepository(
    private val weatherDao: WeatherDao,
    private val apiService: ApiService = RetrofitClient.apiService
) {

    /**
     * Observes weather data from Room as a reactive stream.
     * Always returns cached data immediately if available.
     */
    fun getWeatherStream(): Flow<Weather?> {
        Log.d("WeatherRepository", "Weather data stream initialized from Room")
        return weatherDao.getLatestWeatherFlow().map { entity ->
            entity?.toDomainModel()
        }
    }

    /**
     * Fetches latest weather from Django backend using default/fallback coordinates.
     */
    suspend fun refreshWeather(
        defaultLat: Double = 23.8103,
        defaultLon: Double = 90.4125,
        locationKey: String = "default"
    ): Result<Unit> {
        return refreshWeatherForCoordinates(lat = defaultLat, lon = defaultLon, locationKey = locationKey)
    }

    /**
     * Fetches latest weather from Django backend using GPS latitude & longitude coordinates,
     * and saves it into Room DB.
     */
    suspend fun refreshWeatherForCoordinates(
        lat: Double,
        lon: Double,
        locationKey: String = "default"
    ): Result<Unit> {
        return runCatching {
            Log.d("WeatherRepository", "Fetching fresh weather for lat: $lat, lon: $lon")
            val dto = apiService.getWeather(latitude = lat, longitude = lon)
            val entity = dto.toEntity(locationKey)

            weatherDao.insertWeather(entity)
            Log.d("WeatherRepository", "Successfully saved GPS weather into Room")
            Unit
        }.onFailure { exception ->
            Log.e("WeatherRepository", "Failed to refresh weather for GPS coordinates", exception)
        }
    }
}
