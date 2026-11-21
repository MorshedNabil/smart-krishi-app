package com.nabil.smartkrishi.features.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nabil.smartkrishi.data.local.AppDatabase
import com.nabil.smartkrishi.data.repository.UserRepository
import com.nabil.smartkrishi.data.security.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel managing Login state, input validations, and authentication via POST /auth/accounts/login/.
 */
class LoginViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val tokenManager by lazy { TokenManager(application) }
    private val userRepository: UserRepository by lazy {
        UserRepository(
            userDao = AppDatabase.getInstance(application).userDao(),
            tokenManager = tokenManager
        )
    }

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

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                passwordInput = password,
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

    /**
     * Authenticates user via POST /auth/accounts/login/ with phone number and password (PIN).
     */
    fun loginUser(onLoginSuccess: () -> Unit) {
        val phone = _uiState.value.phoneInput.trim()
        val password = _uiState.value.passwordInput.trim()

        if (phone.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Please enter phone number") }
            return
        }
        if (password.length != 6 || !password.all { it.isDigit() }) {
            _uiState.update { it.copy(errorMessage = "Please enter your valid 6-digit PIN") }
            return
        }

        val formattedPhone = formatPhoneNumber(phone)
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = userRepository.loginUser(formattedPhone, password)
            _uiState.update { it.copy(isLoading = false) }

            if (result.isSuccess) {
                _uiState.update { it.copy(isUserLoggedIn = true) }
                Log.d("LoginViewModel", "Login successful")
                onLoginSuccess()
            } else {
                val errorMsg = result.exceptionOrNull()?.localizedMessage ?: "Login failed. Please check your credentials."
                _uiState.update { it.copy(errorMessage = errorMsg) }
                Log.e("LoginViewModel", "Login failed", result.exceptionOrNull())
            }
        }
    }

    /**
     * format the phone number
     */
    fun formatPhoneNumber(phoneNumber: String): String {
        val digits = phoneNumber.filter { it.isDigit() }

        return when {
            digits.startsWith("880") -> "+$digits"
            digits.startsWith("0") -> "+88$digits"
            else -> "+880$digits"
        }
    }
}
