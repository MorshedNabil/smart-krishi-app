package com.nabil.smartkrishi.data.remote

import com.nabil.smartkrishi.data.remote.dto.DivisionDto
import com.nabil.smartkrishi.data.remote.dto.MarketPriceDto
import com.nabil.smartkrishi.data.remote.dto.UserResponseDto
import com.nabil.smartkrishi.data.remote.dto.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // API for weather data (supports lat/lon GPS coordinates or location query)
    @GET("api/weather/")
    suspend fun getWeather(
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null
    ): WeatherResponseDto

    // API for market Price
    @GET("api/market-prices/latest/")
    suspend fun getMarketPrices(): MarketPriceDto

    // API for fetching administrative location hierarchy (divisions, districts, villages)
    @GET("api/locations/")
    suspend fun getLocations(): List<DivisionDto>

    // API for fetching current authenticated user profile
    @GET("auth/accounts/me/")
    suspend fun getUserProfile(): UserResponseDto
}
