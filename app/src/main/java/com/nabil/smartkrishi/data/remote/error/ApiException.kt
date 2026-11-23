package com.nabil.smartkrishi.data.remote.error

/**
 * Custom Exception encapsulating error code, HTTP status code, server details,
 * and field-level validation errors.
 */
class ApiException(
    val statusCode: Int,
    val errorCode: ApiErrorCode,
    val rawCode: String?,
    val serverMessage: String?,
    val fieldErrors: Map<String, List<String>>? = null
) : Exception(
    serverMessage ?: "${errorCode.defaultUserMessage} ${errorCode.actionSuggestion}".trim()
) {
    /**
     * Formats a user-friendly message based on error code, suggested behavior,
     * and any specific field validation errors returned by the server.
     */
    fun getUserFriendlyMessage(): String {
        return when (errorCode) {
            ApiErrorCode.VALIDATION_ERROR -> {
                if (!fieldErrors.isNullOrEmpty()) {
                    val fieldDetails = fieldErrors.entries.joinToString("\n") { (field, msgs) ->
                        "• $field: ${msgs.joinToString(", ")}"
                    }
                    "Validation Error:\n$fieldDetails"
                } else if (!serverMessage.isNullOrBlank()) {
                    serverMessage
                } else {
                    "${errorCode.defaultUserMessage} ${errorCode.actionSuggestion}"
                }
            }
            ApiErrorCode.PHONE_NUMBER_ALREADY_REGISTERED -> {
                serverMessage ?: "This phone number is already registered. Please log in instead."
            }
            ApiErrorCode.OTP_EXPIRED_OR_INVALID -> {
                serverMessage ?: "No valid, unexpired OTP is available. Please request another OTP."
            }
            ApiErrorCode.INVALID_OTP -> {
                serverMessage ?: "The submitted OTP doesn't match. Please try again."
            }
            ApiErrorCode.INVALID_CREDENTIALS -> {
                serverMessage ?: "Incorrect phone number or password. Please check your sign-in details."
            }
            ApiErrorCode.PRODUCT_ACCESS_DENIED -> {
                serverMessage ?: "You do not have permission to modify or upload images for this product."
            }
            ApiErrorCode.PRODUCT_NOT_FOUND -> {
                serverMessage ?: "The referenced product was not found."
            }
            ApiErrorCode.NOT_FOUND -> {
                serverMessage ?: "No available prices or data found."
            }
            ApiErrorCode.THROTTLED -> {
                serverMessage ?: "Request rate limit exceeded. Please wait before retrying."
            }
            ApiErrorCode.SMS_DELIVERY_FAILED -> {
                serverMessage ?: "SMS provider could not send the OTP. Please try again later."
            }
            ApiErrorCode.OTP_DELIVERY_NOT_CONFIGURED -> {
                serverMessage ?: "OTP delivery is not configured on the server. Service is temporarily unavailable."
            }
            ApiErrorCode.UNKNOWN_ERROR -> {
                serverMessage ?: "An unexpected error occurred (HTTP $statusCode). Please try again."
            }
        }
    }
}
