package com.nabil.smartkrishi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network DTO representing the response from token refresh (POST /auth/accounts/token/refresh/).
 */
@Serializable
data class RefreshTokenResponseDto(
    @SerialName("access")
    val access: String?,

    @SerialName("refresh")
    val refresh: String? = null
)
