package com.nabil.smartkrishi.data.repository

import android.util.Log
import com.nabil.smartkrishi.data.local.dao.UserDao
import com.nabil.smartkrishi.data.local.entity.toDomain
import com.nabil.smartkrishi.data.remote.ApiService
import com.nabil.smartkrishi.data.remote.RetrofitClient
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
            Log.d("UserRepository", "Fetching user profile from API")
            val response = apiService.getUserProfile()
            val userDto = response.user ?: throw Exception("User object is null in API response")

            val entity = userDto.toEntity()
            userDao.clearUsers()
            userDao.insertUser(entity)

            Log.d("UserRepository", "Successfully fetched and cached user profile")
            userDto.toDomain()
        }.onFailure { exception ->
            Log.e("UserRepository", "Failed to fetch user profile", exception)
        }
    }
}
