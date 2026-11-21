package com.nabil.smartkrishi.data.remote

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://10.0.2.2:8000/"

private val json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

object RetrofitClient {
    var authToken: String? = null

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()

            authToken?.let { token ->
                if (token.isNotBlank()) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                    Log.d("RetrofitClient", "Attached JWT Access Token to request: ${originalRequest.url}")
                }
            }

            chain.proceed(requestBuilder.build())
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
