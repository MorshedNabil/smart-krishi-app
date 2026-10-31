package com.nabil.smartkrishi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.nabil.smartkrishi.features.auth.LoginScreen
import com.nabil.smartkrishi.features.auth.SignUp
import com.nabil.smartkrishi.ui.theme.SmartKrishiTheme
import java.util.concurrent.TimeUnit

/**
 * Main Login activity migrating to Jetpack Compose with Firebase Phone Auth.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var verificationCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private var phoneInput by mutableStateOf("")
    private var errorMessage by mutableStateOf<String?>(null)
    private var isEnglish by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        setupPhoneAuthCallbacks()

        val composeView = findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            SmartKrishiTheme {
                LoginScreen(
                    phoneValue = phoneInput,
                    errorMessage = errorMessage,
                    isEnglish = isEnglish,
                    onPhoneChange = {
                        phoneInput = it
                        if (!errorMessage.isNullOrEmpty()) errorMessage = null
                    },
                    onLanguageToggle = { isEnglish = it },
                    onGetOtpClick = { requestPhoneVerification() },
                    onSignUpClick = {
                        val intent = Intent(this@MainActivity, SignUp::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }

    private fun requestPhoneVerification() {
        val phone = phoneInput.trim()
        if (phone.isEmpty()) {
            errorMessage = "Please enter phone number"
            return
        }

        val phoneNumber = "+880$phone"
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(verificationCallbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun setupPhoneAuthCallbacks() {
        verificationCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                errorMessage = e.message ?: "Verification failed"
                Log.e("MainActivity", "Verification failed", e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                val otpIntent = Intent(this@MainActivity, OtpVerification::class.java).apply {
                    putExtra("verification_pin", verificationId)
                }
                startActivity(otpIntent)
            }
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                sendToMain()
            } else {
                errorMessage = task.exception?.message ?: "Sign in failed"
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            sendToMain()
        }
    }

    private fun sendToMain() {
        val intent = Intent(this, OnBoardActivity::class.java)
        startActivity(intent)
        finish()
    }
}
