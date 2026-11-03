package com.nabil.smartkrishi.features.auth

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.TimeUnit

/**
 * UI State for the Login Screen.
 */
data class LoginUiState(
    val phoneInput: String = "",
    val errorMessage: String? = null,
    val isEnglish: Boolean = true,
    val isLoading: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val verificationId: String? = null
)

/**
 * ViewModel managing Login state, input validations, and Firebase Phone Authentication.
 */
class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        checkUserLoggedIn()
    }

    fun checkUserLoggedIn() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _uiState.update { it.copy(isUserLoggedIn = true) }
        }
    }

    fun onPhoneChange(phone: String) {
        _uiState.update {
            it.copy(
                phoneInput = phone,
                errorMessage = null
            )
        }
    }

    fun onLanguageToggle(isEnglish: Boolean) {
        _uiState.update { it.copy(isEnglish = isEnglish) }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun requestPhoneVerification(
        activity: Activity,
        onCodeSent: (verificationId: String) -> Unit
    ) {
        val phone = _uiState.value.phoneInput.trim()
        if (phone.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Please enter phone number") }
            return
        }

        val phoneNumber = "+880$phone"
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Verification failed"
                    )
                }
                Log.e("LoginViewModel", "Verification failed", e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        verificationId = verificationId
                    )
                }
                onCodeSent(verificationId)
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithCredential(
        credential: PhoneAuthCredential,
        onSuccess: () -> Unit = {}
    ) {
        _uiState.update { it.copy(isLoading = true) }
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _uiState.update { it.copy(isLoading = false, isUserLoggedIn = true) }
                onSuccess()
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = task.exception?.message ?: "Sign in failed"
                    )
                }
            }
        }
    }
}
