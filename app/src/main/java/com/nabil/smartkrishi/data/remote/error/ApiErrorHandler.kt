package com.nabil.smartkrishi.data.remote.error

import android.util.Log
import com.nabil.smartkrishi.data.remote.dto.ApiErrorResponse
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import retrofit2.Response

/**
 * Helper utility to parse API error responses from Retrofit, decode JSON bodies,
 * and convert exceptions or failed responses into structured [ApiException]s.
 */
object ApiErrorHandler {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    /**
     * Parses a Retrofit [HttpException] into an [ApiException].
     */
    fun parseHttpException(exception: HttpException): ApiException {
        val statusCode = exception.code()
        val errorBodyString = try {
            exception.response()?.errorBody()?.string()
        } catch (e: Exception) {
            safeLogE("Error reading error body string", e)
            null
        }
        return parseErrorJson(statusCode, errorBodyString)
    }

    /**
     * Parses an unsuccessful Retrofit [Response] into an [ApiException].
     */
    fun <T> parseResponseError(response: Response<T>): ApiException {
        val statusCode = response.code()
        val errorBodyString = try {
            response.errorBody()?.string()
        } catch (e: Exception) {
            safeLogE("Error reading error body string", e)
            null
        }
        return parseErrorJson(statusCode, errorBodyString)
    }

    /**
     * Decodes the raw JSON error response string and maps 'code' and 'status_code' to [ApiException].
     */
    fun parseErrorJson(httpStatusCode: Int, jsonString: String?): ApiException {
        if (jsonString.isNullOrBlank()) {
            val errorCode = ApiErrorCode.fromStatusAndCode(httpStatusCode, null)
            return ApiException(
                statusCode = httpStatusCode,
                errorCode = errorCode,
                rawCode = errorCode.code,
                serverMessage = null
            )
        }

        return try {
            val errorDto = json.decodeFromString<ApiErrorResponse>(jsonString)
            val codeStr = errorDto.code
            val finalStatus = errorDto.statusCode ?: httpStatusCode
            val errorCode = ApiErrorCode.fromStatusAndCode(finalStatus, codeStr)
            val message = errorDto.message ?: errorDto.detail

            ApiException(
                statusCode = finalStatus,
                errorCode = errorCode,
                rawCode = codeStr,
                serverMessage = message,
                fieldErrors = errorDto.errors
            )
        } catch (e: Exception) {
            safeLogE("Failed to parse API error JSON: $jsonString", e)
            val errorCode = ApiErrorCode.fromStatusAndCode(httpStatusCode, null)
            ApiException(
                statusCode = httpStatusCode,
                errorCode = errorCode,
                rawCode = null,
                serverMessage = null
            )
        }
    }

    /**
     * Extracts a human-readable message from any Throwable.
     */
    fun getErrorMessage(throwable: Throwable?): String {
        return when (throwable) {
            is ApiException -> throwable.getUserFriendlyMessage()
            is HttpException -> parseHttpException(throwable).getUserFriendlyMessage()
            is java.net.UnknownHostException,
            is java.net.SocketTimeoutException,
            is java.io.IOException -> "Network connection error. Please check your internet connection."
            else -> throwable?.localizedMessage ?: "An unexpected error occurred."
        }
    }

    /**
     * Executes a suspending network call and wraps the result in [Result],
     * catching [HttpException] and converting it into [ApiException].
     */
    suspend inline fun <T> safeApiCall(crossinline block: suspend () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: HttpException) {
            Result.failure(parseHttpException(e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Executes a suspending call returning Retrofit [Response] and wraps it in [Result],
     * parsing error bodies into [ApiException] if unsuccessful.
     */
    suspend inline fun <reified T> safeResponseCall(crossinline block: suspend () -> Response<T>): Result<T> {
        return try {
            val response = block()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else if (response.code() == 204 || T::class == Unit::class) {
                    @Suppress("UNCHECKED_CAST")
                    Result.success(Unit as T)
                } else {
                    Result.failure(Exception("Response body was null"))
                }
            } else {
                Result.failure(parseResponseError(response))
            }
        } catch (e: HttpException) {
            Result.failure(parseHttpException(e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun safeLogE(message: String, throwable: Throwable? = null) {
        try {
            Log.e("ApiErrorHandler", message, throwable)
        } catch (e: Exception) {
            // Ignored in unit test environment
        }
    }
}
