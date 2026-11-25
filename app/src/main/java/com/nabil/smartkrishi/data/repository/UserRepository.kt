package com.nabil.smartkrishi.data.repository

import android.util.Log
import com.nabil.smartkrishi.data.local.dao.UserDao
import com.nabil.smartkrishi.data.local.entity.toDomain
import com.nabil.smartkrishi.data.remote.ApiService
import com.nabil.smartkrishi.data.remote.RetrofitClient
import com.nabil.smartkrishi.data.remote.dto.LoginRequestDto
import com.nabil.smartkrishi.data.remote.dto.RefreshTokenRequestDto
import com.nabil.smartkrishi.data.remote.dto.SignUpRequestDto
import com.nabil.smartkrishi.data.remote.dto.toDomain
import com.nabil.smartkrishi.data.remote.dto.toEntity
import com.nabil.smartkrishi.data.remote.error.ApiErrorHandler
import com.nabil.smartkrishi.data.security.TokenManager
import com.nabil.smartkrishi.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * Repository acting as Single Source of Truth (SSOT) for User data.
 */
class UserRepository @JvmOverloads constructor(
    private val userDao: UserDao,
    private val apiService: ApiService = RetrofitClient.apiService,
    private val tokenManager: TokenManager? = null
) {
    /*
     * Custom functions for logging in unit test environments.
     */
    private fun safeLogD(message: String) {
        try {
            Log.d("UserRepository", message)
        } catch (e: Exception) {
            // Ignored in unit test environments
        }
    }

    private fun safeLogE(message: String, throwable: Throwable? = null) {
        try {
            Log.e("UserRepository", message, throwable)
        } catch (e: Exception) {
            // Ignored in unit test environments
        }
    }

    /**
     * Observes user data from Room DB as a reactive stream.
     */
    fun getUserStream(): Flow<User?> {
        return userDao.getUserFlow().map { entity ->
            entity?.toDomain()
        }
    }

    /**
     * Fetches current user profile from GET /auth/accounts/me/ API and caches it into Room DB.
     */
    suspend fun refreshUserProfile(): Result<User> {
        return com.nabil.smartkrishi.data.remote.error.ApiErrorHandler.safeApiCall {
            safeLogD("Fetching user profile from API")
            val response = apiService.getUserProfile()
            val userDto = response.user ?: throw Exception("User object is null in API response")

            val entity = userDto.toEntity()
            userDao.clearUsers()
            userDao.insertUser(entity)

            safeLogD("Successfully fetched and cached user profile")
            userDto.toDomain()
        }.onFailure { exception ->
            safeLogE("Failed to fetch user profile", exception)
        }
    }

    /**
     * Authenticates user via POST /auth/accounts/login/ with phone number and password,
     * stores JWT access token, and caches the user profile in Room DB.
     */
    suspend fun loginUser(phone: String, password: String): Result<User> {
        return com.nabil.smartkrishi.data.remote.error.ApiErrorHandler.safeApiCall {
            safeLogD("Logging in user with phone: $phone")
            val requestDto = LoginRequestDto(
                phoneNumber = phone,
                password = password
            )
            val response = apiService.loginUser(requestDto)
            val userDto = response.user ?: throw Exception("User object is null in login response")
            val accessToken = response.tokens?.access
            val refreshToken = response.tokens?.refresh

            if (!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()) {
                tokenManager?.saveTokens(accessToken, refreshToken)
                RetrofitClient.authToken = accessToken
                safeLogD("Successfully saved JWT access and refresh tokens via TokenManager")
            } else if (!accessToken.isNullOrBlank()) {
                RetrofitClient.authToken = accessToken
                safeLogD("Successfully captured JWT access token")
            } else {
                safeLogE("JWT access token is null or empty in login response")
            }

            val entity = userDto.toEntity()
            userDao.clearUsers()
            userDao.insertUser(entity)

            safeLogD("Successfully logged in and cached user profile")
            userDto.toDomain()
        }.onFailure { exception ->
            safeLogE("Failed to login user", exception)
        }
    }

    /**
     * Sends sign-up data to backend API via POST and caches the resulting user profile in Room.
     */
    suspend fun registerUser(
        firstName: String,
        lastName: String,
        phone: String,
        role: String,
        villageId: Int,
        profileImage: String? = null
    ): Result<User> {
        return ApiErrorHandler.safeApiCall {
            safeLogD("Registering user with phone: $phone")
            val requestDto = SignUpRequestDto(
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phone,
                role = role.lowercase(),
                profileImage = profileImage,
                villageId = villageId
            )
            safeLogD("Sending registration request: $requestDto")
            val response = apiService.registerUser(requestDto)
            val userDto = response.user ?: throw Exception("User object is null in registration response")

            val entity = userDto.toEntity()
            userDao.clearUsers()
            userDao.insertUser(entity)

            safeLogD("Successfully registered and cached user profile")
            userDto.toDomain()
        }.onFailure { exception ->
            safeLogE("Failed to register user", exception)
        }
    }

    /**
     * Synchronous blocking wrapper for registerUser, returning null on success,
     * or a human-readable error message String on failure (useful for Java interoperability like OtpVerification).
     */
    fun registerUserSyncOrError(
        firstName: String,
        lastName: String,
        phone: String,
        role: String,
        villageId: Int,
        profileImage: String? = null
    ): String? = runBlocking {
        val result = registerUser(firstName, lastName, phone, role, villageId, profileImage)
        if (result.isSuccess) {
            null
        } else {
            ApiErrorHandler.getErrorMessage(result.exceptionOrNull())
        }
    }

    /**
     * Calls POST /auth/accounts/logout/ API with the refresh token to blacklist it on the server,
     * clears tokens from EncryptedSharedPreferences (TokenManager), resets RetrofitClient.authToken,
     * and clears cached user profile from Room DB.
     */
    suspend fun logoutUser(): Result<Unit> {
        return ApiErrorHandler.safeApiCall {
            val refreshToken = tokenManager?.getRefreshToken()
            safeLogD("Logging out user, refreshToken present: ${!refreshToken.isNullOrBlank()}")

            if (!refreshToken.isNullOrBlank()) {
                val requestDto = RefreshTokenRequestDto(refresh = refreshToken)
                apiService.logoutUser(requestDto)
                safeLogD("Successfully blacklisted JWT refresh token on backend")
            }

            // Clear local tokens and memory session
            tokenManager?.clearTokens()
            RetrofitClient.authToken = null

            // Clear cached user profile from Room DB
            userDao.clearUsers()
            safeLogD("Successfully cleared local user session and database cache")

            Unit
        }.onFailure { exception ->
            safeLogE("Logout API call failed, clearing local tokens as fallback", exception)
            tokenManager?.clearTokens()
            RetrofitClient.authToken = null
            try {
                userDao.clearUsers()
            } catch (e: Exception) {
                safeLogE("Failed to clear local UserDao on logout fallback", e)
            }
        }
    }
}
