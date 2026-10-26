package com.nabil.smartkrishi.data.remote.dto

import com.nabil.smartkrishi.domain.model.MarketPrice
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

// Network DTOs -> Data Transfer Objects
// DTO Purpose: These are used to parse the JSON response from the API
@Serializable
data class MarketPriceDto(
    @SerialName("id")
    val id: Int?,

    @SerialName("product_name")
    val cropTitle: String?,

    @SerialName("price")
    val pricePerKg: String?,

    @SerialName("currency")
    val currency: String?,

    @SerialName("unit")
    val unit: String?,

    @SerialName("source")
    val source: String?,

    @SerialName("market_name")
    val marketName: String?,

    @SerialName("location")
    val marketLocation: String?,

    @SerialName("created_at")
    val createdAt: String?,

    @SerialName("updated_at")
    val updatedAt: String?,

    @SerialName("observed_at")
    val observedAt: String?
)


/* ==================================================================
 * Mapper function converting network DTO to domain MarketPrice object.
 * ==================================================================
 */
fun MarketPriceDto.toMarketPriceDO(): MarketPrice {
    // some values from MarketPriceDto can be ignored. No need to map them, because the app doesn't use them.
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