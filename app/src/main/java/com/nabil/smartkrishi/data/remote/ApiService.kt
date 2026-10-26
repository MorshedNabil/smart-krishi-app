package com.nabil.smartkrishi.data.remote

import com.nabil.smartkrishi.data.remote.dto.MarketPriceDto
import com.nabil.smartkrishi.data.remote.dto.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    // API for weather data
    @GET("api/weather/")
    suspend fun getWeather(@Query("location") location: String = "default"): WeatherResponseDto

    // API for market Price
    @GET("api/market-prices/latest/")
    suspend fun getMarketPrices(): MarketPriceDto

}