package com.nabil.smartkrishi.data.remote

import com.nabil.smartkrishi.data.remote.dto.DivisionDto
import com.nabil.smartkrishi.data.remote.dto.LoginRequestDto
import com.nabil.smartkrishi.data.remote.dto.LoginResponseDto
import com.nabil.smartkrishi.data.remote.dto.MarketPriceDto
import com.nabil.smartkrishi.data.remote.dto.SendOtpRequestDto
import com.nabil.smartkrishi.data.remote.dto.SignUpRequestDto
import com.nabil.smartkrishi.data.remote.dto.UserResponseDto
import com.nabil.smartkrishi.data.remote.dto.WeatherResponseDto
import com.nabil.smartkrishi.data.remote.dto.RefreshTokenRequestDto
import com.nabil.smartkrishi.data.remote.dto.RefreshTokenResponseDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 *  This interface contains all the api services needed in the entire app
 */
interface ApiService {
    // API for weather data (supports lat/lon GPS coordinates or location query)
    @GET("api/weather/")
    suspend fun getWeather(
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null
    ): WeatherResponseDto

    // API for market Price
    @GET("api/market-prices/latest/")
    suspend fun getMarketPrices(): MarketPriceDto

    // API for fetching administrative location hierarchy (divisions, districts, villages)
    @GET("api/locations/")
    suspend fun getLocations(): List<DivisionDto>

    // API for fetching current authenticated user profile
    @GET("auth/accounts/me/")
    suspend fun getUserProfile(): UserResponseDto

    // API for registering new user account
    @POST("auth/accounts/register/")
    suspend fun registerUser(@Body request: SignUpRequestDto): UserResponseDto

    // API for sending OTP message
    @POST("auth/accounts/send-otp/")
    suspend fun sendOtp(@Body request: SendOtpRequestDto): Response<Unit>

    // API for user login
    @POST("auth/accounts/login/")
    suspend fun loginUser(@Body request: LoginRequestDto): LoginResponseDto

    // API for user logout (blacklists the refresh token on the server)
    @POST("auth/accounts/logout/")
    suspend fun logoutUser(@Body request: RefreshTokenRequestDto): Response<Unit>

    // API for refreshing access token using refresh token
    @POST("api/token/refresh/")
    fun refreshToken(@Body request: RefreshTokenRequestDto): Call<RefreshTokenResponseDto>
}
