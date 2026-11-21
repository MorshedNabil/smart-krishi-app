package com.nabil.smartkrishi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nabil.smartkrishi.data.local.AppDatabase
import com.nabil.smartkrishi.data.remote.RetrofitClient
import com.nabil.smartkrishi.data.repository.LocationRepository
import com.nabil.smartkrishi.data.security.TokenManager
import com.nabil.smartkrishi.features.auth.LoginScreen
import com.nabil.smartkrishi.features.auth.SignUpScreen
import com.nabil.smartkrishi.features.profile.ProfileScreen
import com.nabil.smartkrishi.navigation.OnBoardScreen
import com.nabil.smartkrishi.ui.theme.SmartKrishiTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Single Activity entry point for Smart Krishi app hosting Compose Navigation.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val tokenManager = TokenManager(applicationContext)
        RetrofitClient.authToken = tokenManager.getAccessToken()

        // Pre-populate location data into Room DB on app launch
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val locationRepo = LocationRepository(
                    locationDao = AppDatabase.getInstance(applicationContext).locationDao()
                )
                locationRepo.syncLocationsIfNeeded()
            } catch (e: Exception) {
                Log.e("MainActivity", "Failed to sync locations on startup", e)
            }
        }

        val userEntity = runBlocking(Dispatchers.IO) {
            try {
                AppDatabase.getInstance(applicationContext).userDao().getUser()
            } catch (e: Exception) {
                null
            }
        }
        val startDestination = if (userEntity != null) "onboard" else "login"

        setContent {
            SmartKrishiTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate("onboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onSignUpClick = {
                                navController.navigate("signup")
                            }
                        )
                    }

                    composable("signup") {
                        SignUpScreen(
                            onOtpRequested = { verificationId, formData, villageId ->
                                val otpIntent = Intent(this@MainActivity, OtpVerification::class.java).apply {
                                    putExtra("verification_pin", verificationId)
                                    putExtra("is_signup", true)
                                    putExtra("first_name", formData.firstName)
                                    putExtra("last_name", formData.lastName)
                                    putExtra("phone", formData.phone)
                                    putExtra("role", formData.role)
                                    putExtra("village_id", villageId)
                                }
                                startActivity(otpIntent)
                            }
                        )
                    }

                    composable("onboard") {
                        OnBoardScreen(
                            onProfileClick = {
                                navController.navigate("profile")
                            }
                        )
                    }

                    composable("profile") {
                        ProfileScreen(
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onSaveClick = { uiState ->
                                Toast.makeText(
                                    this@MainActivity,
                                    "Profile saved for ${uiState.name} (${uiState.phoneNumber}) - ${uiState.selectedLocation}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onLogoutClick = {
                                tokenManager.clearTokens()
                                lifecycleScope.launch(Dispatchers.IO) {
                                    try {
                                        AppDatabase.getInstance(applicationContext).userDao().clearUsers()
                                        Log.d("MainActivity", "Successfully cleared cached user data and secure tokens on logout")
                                    } catch (e: Exception) {
                                        Log.e("MainActivity", "Failed to clear cached user data on logout", e)
                                    }
                                }
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
