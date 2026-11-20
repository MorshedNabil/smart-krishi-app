package com.nabil.smartkrishi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network DTO representing the sign-up request body for POST /auth/accounts/register/
 */
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
