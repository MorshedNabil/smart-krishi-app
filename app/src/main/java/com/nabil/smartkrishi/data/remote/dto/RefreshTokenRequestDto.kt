package com.nabil.smartkrishi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network DTO representing the request body for token refresh (POST /auth/accounts/token/refresh/).
 */
@Serializable
data class RefreshTokenRequestDto(
    @SerialName("refresh")
    val refresh: String
)
