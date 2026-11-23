package com.nabil.smartkrishi.data.remote.error

/**
 * Enum defining all API error codes with their corresponding HTTP status code,
 * occurrence scenario, and suggested user-facing app behavior/messages.
 */
enum class ApiErrorCode(
    val code: String,
    val httpStatus: Int,
    val defaultUserMessage: String,
    val actionSuggestion: String
) {
    /**
     * HTTP 400 - Request fields or query parameters are invalid.
     * App behavior: Show the relevant field errors.
     */
    VALIDATION_ERROR(
        code = "validation_error",
        httpStatus = 400,
        defaultUserMessage = "Invalid request fields or query parameters.",
        actionSuggestion = "Please check the input fields and try again."
    ),

    /**
     * HTTP 400 - An OTP is requested for a number that already has an account.
     * App behavior: Ask the user to log in instead.
     */
    PHONE_NUMBER_ALREADY_REGISTERED(
        code = "phone_number_already_registered",
        httpStatus = 400,
        defaultUserMessage = "An account is already registered with this phone number.",
        actionSuggestion = "Please log in instead."
    ),

    /**
     * HTTP 400 - No valid, unexpired OTP is available.
     * App behavior: Ask the user to request another OTP.
     */
    OTP_EXPIRED_OR_INVALID(
        code = "otp_expired_or_invalid",
        httpStatus = 400,
        defaultUserMessage = "The OTP has expired or is invalid.",
        actionSuggestion = "Please request another OTP."
    ),

    /**
     * HTTP 400 - The submitted OTP doesn't match.
     * App behavior: Let the user try again.
     */
    INVALID_OTP(
        code = "invalid_otp",
        httpStatus = 400,
        defaultUserMessage = "The submitted OTP is incorrect.",
        actionSuggestion = "Please check the code and try again."
    ),

    /**
     * HTTP 401 - Login phone number or password is incorrect.
     * App behavior: Show a sign-in error.
     */
    INVALID_CREDENTIALS(
        code = "invalid_credentials",
        httpStatus = 401,
        defaultUserMessage = "Incorrect phone number or password.",
        actionSuggestion = "Please verify your credentials and try again."
    ),

    /**
     * HTTP 403 - The user tries to upload an image for someone else's product.
     * App behavior: Show an access-denied message.
     */
    PRODUCT_ACCESS_DENIED(
        code = "product_access_denied",
        httpStatus = 403,
        defaultUserMessage = "Access denied for this product.",
        actionSuggestion = "You do not have permission to modify or upload images for this product."
    ),

    /**
     * HTTP 404 - The product image upload references a missing product.
     * App behavior: Show a not-found message.
     */
    PRODUCT_NOT_FOUND(
        code = "product_not_found",
        httpStatus = 404,
        defaultUserMessage = "Product not found.",
        actionSuggestion = "The requested product does not exist or has been removed."
    ),

    /**
     * HTTP 404 - The latest market-price endpoint has no available prices.
     * App behavior: Show an empty-state message.
     */
    NOT_FOUND(
        code = "not_found",
        httpStatus = 404,
        defaultUserMessage = "No prices or data available.",
        actionSuggestion = "No market price data is available at this time."
    ),

    /**
     * HTTP 429 - The request exceeds an API rate limit.
     * App behavior: Ask the user to wait before retrying.
     */
    THROTTLED(
        code = "throttled",
        httpStatus = 429,
        defaultUserMessage = "Too many requests.",
        actionSuggestion = "You have exceeded the rate limit. Please wait before retrying."
    ),

    /**
     * HTTP 502 - The SMS provider could not send the OTP.
     * App behavior: Offer retry later.
     */
    SMS_DELIVERY_FAILED(
        code = "sms_delivery_failed",
        httpStatus = 502,
        defaultUserMessage = "SMS provider could not send the OTP.",
        actionSuggestion = "Please try again later."
    ),

    /**
     * HTTP 503 - OTP delivery is not configured on the server.
     * App behavior: Show that the service is temporarily unavailable.
     */
    OTP_DELIVERY_NOT_CONFIGURED(
        code = "otp_delivery_not_configured",
        httpStatus = 503,
        defaultUserMessage = "OTP delivery is not configured on the server.",
        actionSuggestion = "Service is temporarily unavailable. Please try again later."
    ),

    /**
     * Fallback for unmapped or generic errors.
     */
    UNKNOWN_ERROR(
        code = "unknown_error",
        httpStatus = 0,
        defaultUserMessage = "An unexpected error occurred.",
        actionSuggestion = "Please try again."
    );

    companion object {
        /**
         * Resolves the [ApiErrorCode] from string code.
         */
        fun fromCode(codeStr: String?): ApiErrorCode? {
            if (codeStr.isNullOrBlank()) return null
            return entries.firstOrNull { it.code.equals(codeStr, ignoreCase = true) }
        }

        /**
         * Resolves the [ApiErrorCode] matching both status_code and code,
         * falling back to status_code mapping if code is not provided.
         */
        fun fromStatusAndCode(statusCode: Int, codeStr: String?): ApiErrorCode {
            fromCode(codeStr)?.let { return it }

            return when (statusCode) {
                400 -> VALIDATION_ERROR
                401 -> INVALID_CREDENTIALS
                403 -> PRODUCT_ACCESS_DENIED
                404 -> NOT_FOUND
                429 -> THROTTLED
                502 -> SMS_DELIVERY_FAILED
                503 -> OTP_DELIVERY_NOT_CONFIGURED
                else -> UNKNOWN_ERROR
            }
        }
    }
}
