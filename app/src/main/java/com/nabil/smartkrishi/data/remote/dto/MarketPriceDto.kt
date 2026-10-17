package com.nabil.smartkrishi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Network DTOs -> Data Transfer Objects
// These are used to parse the JSON response from the API
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