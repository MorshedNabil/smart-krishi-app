package com.nabil.smartkrishi.data.security

import android.util.Log
import com.nabil.smartkrishi.data.remote.RetrofitClient
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import com.nabil.smartkrishi.data.remote.dto.RefreshTokenRequestDto

/**
 * Interceptor/Authenticator class that checks request endpoints and includes the JWT Authorization bearer token
 * only for protected endpoints.
 *
 * Automatically skips attaching Authorization headers for:
 * - Login endpoints (e.g., /auth/accounts/login/)
 * - Signup / Registration / OTP endpoints (e.g., /auth/accounts/register/, /auth/accounts/send-otp/)
 * - Token Refresh endpoints (e.g., /auth/accounts/token/refresh/)
 */
class JWTAuthenticator(
    private val tokenProvider: () -> String? = { RetrofitClient.authToken },
    private val tokenManager: () -> TokenManager? = { RetrofitClient.tokenManager }
) : Interceptor, Authenticator {

    // Called by OkHttp BEFORE request is sent
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val path = originalRequest.url.encodedPath

        val requestBuilder = originalRequest.newBuilder()

        if (shouldSkipToken(path)) {
            safeLogD("Public endpoint detected ($path). Bypassing Authorization header.")
        } else {
            val token = tokenProvider()
            if (!token.isNullOrBlank()) {
                requestBuilder.header("Authorization", "Bearer $token")
                safeLogD("Attached JWT Access Token to request: $path")
            } else {
                safeLogD("No JWT token available for protected endpoint: $path")
            }
        }

        return chain.proceed(requestBuilder.build())
    }

    // Called AFTER receiving HTTP 401 Unauthorized
    override fun authenticate(route: Route?, response: Response): Request? {
        val path = response.request.url.encodedPath

        // Prevent infinite retry loops if token refresh or public endpoint failed with 401
        if (shouldSkipToken(path)) {
            return null
        }

        synchronized(this) {
            val currentToken = tokenProvider()
            val requestToken = response.request.header("Authorization")?.removePrefix("Bearer ")

            // If another thread already refreshed the token, retry with updated token immediately
            if (!currentToken.isNullOrBlank() && currentToken != requestToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            val refreshToken = tokenManager()?.getRefreshToken() ?: return null

            return try {
                // Call api 'api/token/refresh' to get the new refresh and access token
                // This need to be a synchronous call otherwise multiple call for refresh token will generate for multiple API failure
                val refreshResponse = RetrofitClient.apiService.refreshToken(
                    RefreshTokenRequestDto(refresh = refreshToken)
                ).execute()

                if (refreshResponse.isSuccessful) {
                    val newTokens = refreshResponse.body()
                    val newAccessToken = newTokens?.access

                    if (!newAccessToken.isNullOrBlank()) {
                        val newRefreshToken = newTokens.refresh ?: refreshToken
                        tokenManager()?.saveTokens(newAccessToken, newRefreshToken) // save the new tokens in encrypted SharedPreferences
                        RetrofitClient.authToken = newAccessToken

                        safeLogD("Successfully refreshed JWT token automatically on 401")
                        response.request.newBuilder()
                            .header("Authorization", "Bearer $newAccessToken")
                            .build()
                    } else {
                        null
                    }
                } else {
                    safeLogE("Token refresh failed with HTTP ${refreshResponse.code()}")
                    null
                }
            } catch (e: Exception) {
                safeLogE("Exception during automatic token refresh", e)
                null
            }
        }
    }

    /**
     * Checks if the endpoint path is for login, signup/registration, or token refresh.
     */
    fun shouldSkipToken(path: String): Boolean {
        val lowerPath = path.lowercase()
        return isLoginEndpoint(lowerPath) ||
                isSignupEndpoint(lowerPath) ||
                isRefreshTokenEndpoint(lowerPath)
    }

    /**
     * Checks if the path is for login.
     */
    fun isLoginEndpoint(path: String): Boolean {
        val lower = path.lowercase()
        return lower.contains("/login") || lower.contains("auth/accounts/login")
    }

    /**
     * Checks if the path is for signup, registration, or sending OTP.
     */
    fun isSignupEndpoint(path: String): Boolean {
        val lower = path.lowercase()
        return lower.contains("/register") ||
                lower.contains("auth/accounts/register") ||
                lower.contains("/send-otp") ||
                lower.contains("auth/accounts/send-otp")
    }

    /**
     * Checks if the path is for token refresh.
     */
    fun isRefreshTokenEndpoint(path: String): Boolean {
        val lower = path.lowercase()
        return lower.contains("/refresh") ||
                lower.contains("token/refresh") ||
                lower.contains("auth/accounts/token/refresh")
    }

    private fun safeLogD(message: String) {
        try {
            Log.d("JWTAuthenticator", message)
        } catch (e: Exception) {
            // Ignored in unit test environment
        }
    }

    private fun safeLogE(message: String, throwable: Throwable? = null) {
        try {
            Log.e("JWTAuthenticator", message, throwable)
        } catch (e: Exception) {
            // Ignored in unit test environment
        }
    }
}
