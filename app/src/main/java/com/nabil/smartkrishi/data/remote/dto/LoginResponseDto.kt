package com.nabil.smartkrishi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network DTO representing the login response for POST /auth/accounts/login/
 */
@Serializable
data class LoginResponseDto(
    @SerialName("message")
    val message: String?,

    @SerialName("user")
    val user: UserDto?,

    @SerialName("tokens")
    val tokens: TokenDto?
)

@Serializable
data class TokenDto(
    @SerialName("access")
    val access: String?,

    @SerialName("refresh")
    val refresh: String?
)
