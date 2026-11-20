package com.nabil.smartkrishi.features.auth

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel managing Login state and input validations.
 */
class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

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

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        // Simulate code sent for backend JWT / OTP flow
        _uiState.update {
            it.copy(
                isLoading = false,
                verificationId = "simulated_verification_id"
            )
        }
        Log.d("LoginViewModel", "Code sent successfully")
        onCodeSent("simulated_verification_id")
    }
}
