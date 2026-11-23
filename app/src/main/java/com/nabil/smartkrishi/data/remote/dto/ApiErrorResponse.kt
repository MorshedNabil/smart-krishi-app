package com.nabil.smartkrishi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO representing standard error responses returned by the API.
 * Contains the 'code' and 'status_code' fields along with messages and validation error details.
 */
@Serializable
data class ApiErrorResponse(
    @SerialName("code")
    val code: String? = null,

    @SerialName("status_code")
    val statusCode: Int? = null,

    @SerialName("message")
    val message: String? = null,

    @SerialName("detail")
    val detail: String? = null,

    @SerialName("errors")
    val errors: Map<String, List<String>>? = null
)
