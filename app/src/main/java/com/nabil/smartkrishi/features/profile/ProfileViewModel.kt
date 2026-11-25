package com.nabil.smartkrishi.features.profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nabil.smartkrishi.data.local.AppDatabase
import com.nabil.smartkrishi.data.repository.UserRepository
import com.nabil.smartkrishi.data.security.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import kotlinx.coroutines.flow.update

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val tokenManager by lazy { TokenManager(application) }
    private val userRepository: UserRepository by lazy {
        UserRepository(
            userDao = AppDatabase.getInstance(application).userDao(),
            tokenManager = tokenManager
        )
    }

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun updatePhone(phone: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = phone)
    }

    fun updateVillage(village: String) {
        _uiState.value = _uiState.value.copy(selectedLocation = village)
    }

    fun toggleLocationDropdown(expanded: Boolean) {
        _uiState.value = _uiState.value.copy(isLocationDropdownExpanded = expanded)
    }

    fun updateProfileImage(uri: Uri) {
        _uiState.value = _uiState.value.copy(profileImageUri = uri)
    }

    /**
     * Performs user logout by calling POST auth/accounts/logout/ to blacklist the token on backend,
     * clears local EncryptedSharedPreferences tokens, and resets local session.
     * Sets isLoggingOut = true to trigger loading animation on the LogoutButton.
     */
    fun logoutUser(onLogoutComplete: () -> Unit) {
        _uiState.update { it.copy(isLoggingOut = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.logoutUser()
            } finally {
                withContext(Dispatchers.Main) {
                    _uiState.update { it.copy(isLoggingOut = false) }
                    onLogoutComplete()
                }
            }
        }
    }
}
