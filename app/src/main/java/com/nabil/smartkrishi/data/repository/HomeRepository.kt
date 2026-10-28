package com.nabil.smartkrishi.data.repository

import android.util.Log
import com.nabil.smartkrishi.data.remote.ApiService
import com.nabil.smartkrishi.data.remote.RetrofitClient
import com.nabil.smartkrishi.data.remote.dto.toMarketPriceDO
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
            val marketPriceDTO = apiService.getMarketPrices()
            Log.d("HomeRepository", "getMarketPrices: $marketPriceDTO")
            marketPriceDTO.toMarketPriceDO()
        }
    }

}


