package com.nabil.smartkrishi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Authentication DTOs representing request and response payloads for login, signup, OTP, and token refresh endpoints.
 */

@Serializable
data class LoginRequestDto(
    @SerialName("phone_number")
    val phoneNumber: String,

    @SerialName("password")
    val password: String
)

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

@Serializable
data class SignUpRequestDto(
    @SerialName("first_name")
    val firstName: String,

    @SerialName("last_name")
    val lastName: String,

    @SerialName("phone_number")
    val phoneNumber: String,

    @SerialName("role")
    val role: String? = "farmer",

    @SerialName("profile_image")
    val profileImage: String? = null,

    @SerialName("village_id")
    val villageId: Int? = null
)

@Serializable
data class SendOtpRequestDto(
    @SerialName("phone_number")
    val phoneNumber: String
)

@Serializable
data class RefreshTokenRequestDto(
    @SerialName("refresh")
    val refresh: String
)

@Serializable
data class RefreshTokenResponseDto(
    @SerialName("access")
    val access: String?,

    @SerialName("refresh")
    val refresh: String? = null
)
