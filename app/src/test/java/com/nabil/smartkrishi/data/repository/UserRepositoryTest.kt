package com.nabil.smartkrishi.data.repository

import com.nabil.smartkrishi.data.local.dao.UserDao
import com.nabil.smartkrishi.data.local.entity.UserEntity
import com.nabil.smartkrishi.data.remote.ApiService
import com.nabil.smartkrishi.data.remote.dto.DivisionDto
import com.nabil.smartkrishi.data.remote.dto.MarketPriceDto
import com.nabil.smartkrishi.data.remote.dto.SignUpRequestDto
import com.nabil.smartkrishi.data.remote.dto.UserDto
import com.nabil.smartkrishi.data.remote.dto.UserResponseDto
import com.nabil.smartkrishi.data.remote.dto.WeatherResponseDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit Test for [UserRepository] signup functionality using fake implementations.
 */
class UserRepositoryTest {

    private lateinit var fakeUserDao: FakeUserDao
    private lateinit var fakeApiService: FakeApiService
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        fakeUserDao = FakeUserDao()
        fakeApiService = FakeApiService()
        userRepository = UserRepository(fakeUserDao, fakeApiService)
    }

    @Test
    fun registerUser_withDummyData_success_returnsUser() = runBlocking {
        val result = userRepository.registerUser(
            firstName = "John",
            lastName = "Doe",
            phone = "01712345678",
            division = "Dhaka",
            district = "Dhaka",
            villageName = "Rahitpur",
            role = "Farmer"
        )

        assertTrue(result.isSuccess)
        val user = result.getOrNull()
        assertEquals("John", user?.firstName)
        assertEquals("Doe", user?.lastName)
        assertEquals("01712345678", user?.phone)
        assertEquals("farmer", user?.role)
        assertEquals(10, user?.villageId)
        assertTrue(fakeUserDao.insertedUsers.isNotEmpty())
        assertEquals(1, fakeUserDao.clearedCount)
    }
}

class FakeUserDao : UserDao {
    val insertedUsers = mutableListOf<UserEntity>()
    var clearedCount = 0

    override fun getUserFlow(): Flow<UserEntity?> = flowOf(insertedUsers.firstOrNull())

    override suspend fun getUser(): UserEntity? = insertedUsers.firstOrNull()

    override suspend fun insertUser(user: UserEntity): Long {
        insertedUsers.add(user)
        return 1L
    }

    override suspend fun updateUser(user: UserEntity) {
        if (insertedUsers.isNotEmpty()) {
            insertedUsers[0] = user
        }
    }

    override suspend fun clearUsers() {
        clearedCount++
        insertedUsers.clear()
    }
}

class FakeApiService : ApiService {
    var lastSignUpRequest: SignUpRequestDto? = null

    override suspend fun getWeather(latitude: Double?, longitude: Double?): WeatherResponseDto {
        throw NotImplementedError()
    }

    override suspend fun getMarketPrices(): MarketPriceDto {
        throw NotImplementedError()
    }

    override suspend fun getLocations(): List<DivisionDto> {
        throw NotImplementedError()
    }

    override suspend fun getUserProfile(): UserResponseDto {
        throw NotImplementedError()
    }

    override suspend fun registerUser(request: SignUpRequestDto): UserResponseDto {
        lastSignUpRequest = request
        return UserResponseDto(
            user = UserDto(
                id = 1,
                firebaseUid = "fb-uid-123",
                phoneNumber = request.phoneNumber,
                firstName = request.firstName,
                lastName = request.lastName,
                role = request.role,
                profileImage = "https://example.com/img.jpg",
                villageId = 10,
                createdAt = "2026-03-30",
                updatedAt = "2026-03-30"
            )
        )
    }
}
