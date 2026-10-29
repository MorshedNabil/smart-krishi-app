package com.nabil.smartkrishi.data.remote

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://10.0.2.2:8000/" // the base url for the api. It is currently in localhost. For physical devices use ip address of PC instead.

private val json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

/**
 * OkHttpClient configured with an Auth Interceptor that automatically retrieves
 * the Firebase ID Token of the currently logged-in user and attaches it as a Bearer token
 * in the Authorization header for API requests.
 */
private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .addInterceptor { chain ->
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            try {
                // Fetch Firebase ID token synchronously (safe because Interceptor runs on background I/O thread)
                val tokenResult = Tasks.await(currentUser.getIdToken(false))
                val token = tokenResult.token
                if (!token.isNullOrEmpty()) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                    Log.d("RetrofitClient", "Attached Firebase ID Token to request: ${originalRequest.url}")
                }
            } catch (e: Exception) {
                Log.e("RetrofitClient", "Failed to retrieve Firebase ID Token", e)
            }
        } else {
            Log.w("RetrofitClient", "No authenticated user found in FirebaseAuth for request: ${originalRequest.url}")
        }

        chain.proceed(requestBuilder.build())
    }
    .build()

// retrofit initialize
private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

// initialize api service for this app
// it is a singleton object that can be accessed from anywhere in the app
object RetrofitClient {
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
