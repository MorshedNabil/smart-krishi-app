package com.nabil.smartkrishi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.nabil.smartkrishi.features.auth.LoginScreen
import com.nabil.smartkrishi.features.auth.SignUpScreen
import com.nabil.smartkrishi.features.profile.ProfileScreen
import com.nabil.smartkrishi.navigation.OnBoardScreen
import com.nabil.smartkrishi.ui.theme.SmartKrishiTheme

/**
 * Single Activity entry point for Smart Krishi app hosting Compose Navigation.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val currentUser = FirebaseAuth.getInstance().currentUser
        val startDestination = if (currentUser != null) "onboard" else "login"

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
                            onGetOtpClick = { verificationId ->
                                val otpIntent = Intent(this@MainActivity, OtpVerification::class.java).apply {
                                    putExtra("verification_pin", verificationId)
                                }
                                startActivity(otpIntent)
                            },
                            onSignUpClick = {
                                navController.navigate("signup")
                            }
                        )
                    }

                    composable("signup") {
                        SignUpScreen(
                            onSignUpClick = {
                                navController.navigate("onboard") {
                                    popUpTo("login") { inclusive = true }
                                }
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
                            }
                        )
                    }
                }
            }
        }
    }
}
