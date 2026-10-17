package com.nabil.smartkrishi.data.repository

import android.util.Log
import com.nabil.smartkrishi.data.remote.ApiService
import com.nabil.smartkrishi.data.remote.RetrofitClient
import com.nabil.smartkrishi.data.remote.dto.MarketPriceDto
import com.nabil.smartkrishi.domain.model.MarketPrice

/**
 * HomeRepository acts as the single source of truth for home screen data.
 * It fetches DTOs from ApiService, maps them into domain objects, and provides error safety.
 */
class HomeRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {

    /**
     * Fetches market price from the API and converts DTO to domain MarketPrice object.
     */
    suspend fun getMarketPrices(): Result<MarketPrice> {
        return runCatching {
            val dto = apiService.getMarketPrices()
            Log.d("HomeRepository", "getMarketPrices: $dto")
            dto.toMarketPriceDO()
        }
    }

    /**
     * Fetches weather details for the specified location.
     */
    suspend fun getWeather(location: String): Result<String> {
        return runCatching {
            apiService.getWeather(location)
        }
    }

    /**
     * Fetches available farmer tools.
     */
    suspend fun getFarmerTools(): Result<String> {
        return runCatching {
            apiService.getFarmerTools()
        }
    }
}

/**
 * Mapper function converting network DTO to domain MarketPrice object.
 */
fun MarketPriceDto.toMarketPriceDO(): MarketPrice {
    val numericPrice = pricePerKg?.toDoubleOrNull() ?: 0.0

    return MarketPrice(
        id = id ?: 0,
        cropTitle = cropTitle ?: "No Crop price for today",
        price = numericPrice,
        formattedPrice = "৳ $numericPrice/kg",
        marketName = marketName ?: "No Market Name",
        marketLocation = marketLocation ?: "No Market Location",
        createdAt = createdAt ?: "None"
    )
}
