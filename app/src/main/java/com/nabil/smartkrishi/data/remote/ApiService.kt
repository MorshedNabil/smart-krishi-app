package com.nabil.smartkrishi.data.remote

import com.nabil.smartkrishi.data.remote.dto.MarketPriceDto
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("api/weather")
    suspend fun getWeather(@Query("location") location: String): String

    @GET("api/market-prices/latest/")
    suspend fun getMarketPrices(): MarketPriceDto

    @GET("api/farmer-tools")
    suspend fun getFarmerTools(): String
}