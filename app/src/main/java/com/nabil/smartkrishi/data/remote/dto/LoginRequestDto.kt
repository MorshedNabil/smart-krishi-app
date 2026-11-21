package com.nabil.smartkrishi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network DTO representing the login request body for POST /auth/accounts/login/
 */
@Serializable
data class LoginRequestDto(
    @SerialName("phone_number")
    val phoneNumber: String,

    @SerialName("password")
    val password: String
)
