package com.nabil.smartkrishi.data.repository

import android.util.Log
import com.nabil.smartkrishi.data.local.dao.UserDao
import com.nabil.smartkrishi.data.local.entity.toDomain
import com.nabil.smartkrishi.data.remote.ApiService
import com.nabil.smartkrishi.data.remote.RetrofitClient
import com.nabil.smartkrishi.data.remote.dto.SignUpRequestDto
import com.nabil.smartkrishi.data.remote.dto.toDomain
import com.nabil.smartkrishi.data.remote.dto.toEntity
import com.nabil.smartkrishi.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository acting as Single Source of Truth (SSOT) for User data.
 */
class UserRepository(
    private val userDao: UserDao,
    private val apiService: ApiService = RetrofitClient.apiService
) {

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
        return runCatching {
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
     * Sends sign-up data to backend API via POST and caches the resulting user profile in Room.
     */
    suspend fun registerUser(
        firstName: String,
        lastName: String,
        phone: String,
        division: String,
        district: String,
        villageName: String,
        role: String
    ): Result<User> {
        return runCatching {
            safeLogD("Registering user with phone: $phone")
            val requestDto = SignUpRequestDto(
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phone,
                role = role.lowercase(),
                division = division,
                district = district,
                villageName = villageName
            )
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
}
