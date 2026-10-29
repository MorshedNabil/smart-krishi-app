package com.nabil.smartkrishi.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nabil.smartkrishi.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room Data Access Object (DAO) for weather data caching.
 */
@Dao
interface WeatherDao {

    /**
     * Observes the latest cached weather record reactively.
     */
    @Query("SELECT * FROM weather_data ORDER BY lastUpdated DESC LIMIT 1")
    fun getLatestWeatherFlow(): Flow<WeatherEntity?>

    /**
     * Observes cached weather data for a specific location reactively.
     */
    @Query("SELECT * FROM weather_data WHERE location = :location")
    fun getWeatherFlow(location: String = "default"): Flow<WeatherEntity?>

    /**
     * Fetches cached weather data once asynchronously.
     */
    @Query("SELECT * FROM weather_data WHERE location = :location")
    suspend fun getWeatherOnce(location: String = "default"): WeatherEntity?

    /**
     * Inserts or replaces weather data for a given location.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    /**
     * Deletes cached weather data for a specific location.
     */
    @Query("DELETE FROM weather_data WHERE location = :location")
    suspend fun deleteWeather(location: String = "default")

    /**
     * Clears all cached weather records from Room.
     */
    @Query("DELETE FROM weather_data")
    suspend fun clearAllWeather()
}
