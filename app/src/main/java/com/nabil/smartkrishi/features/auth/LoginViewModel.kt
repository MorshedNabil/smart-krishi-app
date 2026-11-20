package com.nabil.smartkrishi.features.auth

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabil.smartkrishi.data.remote.RetrofitClient
import com.nabil.smartkrishi.data.remote.dto.SendOtpRequestDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

        val formattedPhone = "+880$phone"
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.sendOtp(SendOtpRequestDto(phoneNumber = formattedPhone))
                _uiState.update { it.copy(isLoading = false) }

                if (response.isSuccessful) {
                    _uiState.update { it.copy(verificationId = "otp_sent") }
                    Log.d("LoginViewModel", "OTP Code sent successfully via backend API")
                    onCodeSent("otp_sent")
                } else {
                    _uiState.update { it.copy(errorMessage = "Failed to send OTP. Please check the phone number.") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Failed to send OTP") }
                Log.e("LoginViewModel", "Send OTP failed", e)
            }
        }
    }
}
