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
    fun getWeatherStream(location: String = "default"): Flow<Weather?> {
        return weatherDao.getWeatherFlow(location).map { entity ->
            entity?.toDomainModel()
        }
    }

    /**
     * Fetches latest weather from Django backend and saves it into Room DB.
     * Saving to Room triggers [getWeatherStream] to automatically emit updated data to the UI.
     */
    suspend fun refreshWeather(location: String = "default"): Result<Unit> {
        return runCatching {
            Log.d("WeatherRepository", "Fetching fresh weather for location: $location")
            val dto = apiService.getWeather(location) // API call for weather data
            val entity = dto.toEntity(location)
            
            weatherDao.insertWeather(entity) // Store new data into Room DB
            Log.d("WeatherRepository", "Successfully saved updated weather into Room")
            Unit
        }.onFailure { exception ->
            Log.e("WeatherRepository", "Failed to refresh weather from backend", exception)
        }
    }
}
