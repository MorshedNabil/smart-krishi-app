package com.nabil.smartkrishi.domain.model

// Domain object representing Market Price in the application
data class MarketPrice(
    val id: Int,
    val cropTitle: String,
    val price: Double,
    val formattedPrice: String,
    val marketName: String,
    val marketLocation: String,
    val createdAt: String
)
