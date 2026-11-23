package com.nabil.smartkrishi.data.remote.error

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [ApiErrorHandler], [ApiErrorCode], and [ApiException].
 * Verifies parsing of HTTP status codes and error code strings according to the backend specification.
 */
class ApiErrorHandlerTest {

    @Test
    fun parseValidationError_code400_returnsValidationErrorException() {
        val json = """
            {
                "status_code": 400,
                "code": "validation_error",
                "message": "Invalid form input",
                "errors": {
                    "phone_number": ["This field is required."],
                    "first_name": ["Must not be blank."]
                }
            }
        """.trimIndent()

        val exception = ApiErrorHandler.parseErrorJson(400, json)

        assertEquals(400, exception.statusCode)
        assertEquals(ApiErrorCode.VALIDATION_ERROR, exception.errorCode)
        assertEquals("validation_error", exception.rawCode)
        assertNotNull(exception.fieldErrors)
        assertTrue(exception.getUserFriendlyMessage().contains("phone_number"))
    }

    @Test
    fun parsePhoneNumberAlreadyRegistered_code400_returnsSuggestedLoginMessage() {
        val json = """
            {
                "status_code": 400,
                "code": "phone_number_already_registered",
                "message": "An account already exists for this phone number."
            }
        """.trimIndent()

        val exception = ApiErrorHandler.parseErrorJson(400, json)

        assertEquals(400, exception.statusCode)
        assertEquals(ApiErrorCode.PHONE_NUMBER_ALREADY_REGISTERED, exception.errorCode)
        assertTrue(exception.getUserFriendlyMessage().contains("account already exists"))
    }

    @Test
    fun parseOtpExpiredOrInvalid_code400_returnsSuggestedRequestAnotherOtpMessage() {
        val json = """
            {
                "status_code": 400,
                "code": "otp_expired_or_invalid",
                "message": "OTP expired."
            }
        """.trimIndent()

        val exception = ApiErrorHandler.parseErrorJson(400, json)

        assertEquals(400, exception.statusCode)
        assertEquals(ApiErrorCode.OTP_EXPIRED_OR_INVALID, exception.errorCode)
        assertTrue(exception.getUserFriendlyMessage().contains("OTP expired"))
    }

    @Test
    fun parseInvalidOtp_code400_returnsTryAgainMessage() {
        val json = """
            {
                "status_code": 400,
                "code": "invalid_otp",
                "message": "OTP does not match."
            }
        """.trimIndent()

        val exception = ApiErrorHandler.parseErrorJson(400, json)

        assertEquals(400, exception.statusCode)
        assertEquals(ApiErrorCode.INVALID_OTP, exception.errorCode)
        assertTrue(exception.getUserFriendlyMessage().contains("OTP does not match"))
    }

    @Test
    fun parseInvalidCredentials_code401_returnsSignInErrorMessage() {
        val json = """
            {
                "status_code": 401,
                "code": "invalid_credentials",
                "message": "Invalid phone number or password."
            }
        """.trimIndent()

        val exception = ApiErrorHandler.parseErrorJson(401, json)

        assertEquals(401, exception.statusCode)
        assertEquals(ApiErrorCode.INVALID_CREDENTIALS, exception.errorCode)
        assertTrue(exception.getUserFriendlyMessage().contains("Invalid phone number or password"))
    }

    @Test
    fun parseProductAccessDenied_code403_returnsAccessDeniedMessage() {
        val json = """
            {
                "status_code": 403,
                "code": "product_access_denied",
                "message": "Permission denied for this product."
            }
        """.trimIndent()

        val exception = ApiErrorHandler.parseErrorJson(403, json)

        assertEquals(403, exception.statusCode)
        assertEquals(ApiErrorCode.PRODUCT_ACCESS_DENIED, exception.errorCode)
        assertTrue(exception.getUserFriendlyMessage().contains("Permission denied"))
    }

    @Test
    fun parseProductNotFound_code404_returnsProductNotFoundMessage() {
        val json = """
            {
                "status_code": 404,
                "code": "product_not_found",
                "message": "Product image references a missing product."
            }
        """.trimIndent()

        val exception = ApiErrorHandler.parseErrorJson(404, json)

        assertEquals(404, exception.statusCode)
        assertEquals(ApiErrorCode.PRODUCT_NOT_FOUND, exception.errorCode)
        assertTrue(exception.getUserFriendlyMessage().contains("Product image references a missing product"))
    }

    @Test
    fun parseNotFound_code404_returnsEmptyStateMessage() {
        val json = """
            {
                "status_code": 404,
                "code": "not_found",
                "message": "No available market prices."
            }
        """.trimIndent()

        val exception = ApiErrorHandler.parseErrorJson(404, json)

        assertEquals(404, exception.statusCode)
        assertEquals(ApiErrorCode.NOT_FOUND, exception.errorCode)
        assertTrue(exception.getUserFriendlyMessage().contains("No available market prices"))
    }

    @Test
    fun parseThrottled_code429_returnsRateLimitMessage() {
        val json = """
            {
                "status_code": 429,
                "code": "throttled",
                "message": "Request limit exceeded."
            }
        """.trimIndent()

        val exception = ApiErrorHandler.parseErrorJson(429, json)

        assertEquals(429, exception.statusCode)
        assertEquals(ApiErrorCode.THROTTLED, exception.errorCode)
        assertTrue(exception.getUserFriendlyMessage().contains("Request limit exceeded"))
    }

    @Test
    fun parseSmsDeliveryFailed_code502_returnsSmsFailedMessage() {
        val json = """
            {
                "status_code": 502,
                "code": "sms_delivery_failed",
                "message": "SMS provider failed to send OTP."
            }
        """.trimIndent()

        val exception = ApiErrorHandler.parseErrorJson(502, json)

        assertEquals(502, exception.statusCode)
        assertEquals(ApiErrorCode.SMS_DELIVERY_FAILED, exception.errorCode)
        assertTrue(exception.getUserFriendlyMessage().contains("SMS provider failed"))
    }

    @Test
    fun parseOtpDeliveryNotConfigured_code503_returnsServiceUnavailableMessage() {
        val json = """
            {
                "status_code": 503,
                "code": "otp_delivery_not_configured",
                "message": "OTP delivery not configured on server."
            }
        """.trimIndent()

        val exception = ApiErrorHandler.parseErrorJson(503, json)

        assertEquals(503, exception.statusCode)
        assertEquals(ApiErrorCode.OTP_DELIVERY_NOT_CONFIGURED, exception.errorCode)
        assertTrue(exception.getUserFriendlyMessage().contains("OTP delivery not configured"))
    }

    @Test
    fun parseUnknownCode_fallsBackToStatusMapping() {
        val json = """
            {
                "status_code": 400,
                "code": "unrecognized_code",
                "message": "Some custom 400 error."
            }
        """.trimIndent()

        val exception = ApiErrorHandler.parseErrorJson(400, json)

        assertEquals(400, exception.statusCode)
        assertEquals(ApiErrorCode.VALIDATION_ERROR, exception.errorCode)
    }
}
