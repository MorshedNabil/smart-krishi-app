package com.nabil.smartkrishi.domain.model

// Domain object(DO) representing Market Price in the application
data class MarketPrice(
    // not all the values of MarketPriceDto are used in the app. so some values can be ignored while mapping
    val id: Int,
    val cropTitle: String,
    val price: Double,
    val formattedPrice: String,
    val marketName: String,
    val marketLocation: String,
    val createdAt: String
)
