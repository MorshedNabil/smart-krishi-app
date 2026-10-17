package com.nabil.smartkrishi.data.repository

import android.util.Log
import com.nabil.smartkrishi.data.remote.ApiService
import com.nabil.smartkrishi.data.remote.RetrofitClient
import com.nabil.smartkrishi.data.remote.dto.MarketPriceDto
import com.nabil.smartkrishi.domain.model.MarketPrice
import java.util.Locale
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.ZoneId

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
    // some values from MarketPriceDto can be ignored. No need to map them because the app doesn't use them.
    val numericPrice = pricePerKg?.toDoubleOrNull() ?: 0.0

    return MarketPrice(
        id = id ?: 0,
        cropTitle = cropTitle ?: "No Crop price for today",
        price = numericPrice,
        formattedPrice = "৳ $numericPrice/${unit ?: "kg"}",
        marketName = marketName ?: "No Market Name",
        marketLocation = marketLocation ?: "No Market Location",
        createdAt = formatFormattedDate(createdAt)
    )
}

/**
 * Helper function to safely parse and format ISO/Timestamp date strings (e.g. "2025-01-12T10:30:00Z")
 * into a clean date string (e.g. "12 Jan, 2025").
 */
private fun formatFormattedDate(rawDate: String?): String {
    if (rawDate.isNullOrBlank()) return "None"
    return try {
        // Try parsing full ISO-8601 timestamp (e.g. "2025-01-12T10:30:00Z")
        val instant = java.time.Instant.parse(rawDate)
        val zonedDateTime = instant.atZone(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale.ENGLISH)
        zonedDateTime.format(formatter)
    } catch (e: Exception) {
        try {
            // Fallback: Try parsing date without time (e.g. "2025-01-12")
            val datePart = rawDate.take(10) // "2025-01-12"
            val localDate = LocalDate.parse(datePart)
            val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale.ENGLISH)
            localDate.format(formatter)
        } catch (e2: Exception) {
            if (rawDate.length >= 10) rawDate.substring(0, 10) else rawDate
        }
    }
}
