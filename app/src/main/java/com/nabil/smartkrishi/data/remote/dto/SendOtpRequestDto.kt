package com.nabil.smartkrishi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network DTO representing the request body for POST /auth/accounts/send-otp/
 */
@Serializable
data class SendOtpRequestDto(
    @SerialName("phone_number")
    val phoneNumber: String
)
