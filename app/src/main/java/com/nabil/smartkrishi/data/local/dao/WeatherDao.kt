package com.nabil.smartkrishi.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nabil.smartkrishi.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room Data Access Object (DAO) for weather data caching.
 * Dao purpose: to define the methods to access the database.
 */
@Dao
interface WeatherDao {

    /**
     * Observes cached weather data for a specific location reactively.
     * Room will automatically emit updated data through Flow whenever the database changes.
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
