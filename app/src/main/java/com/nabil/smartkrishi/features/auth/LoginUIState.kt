package com.nabil.smartkrishi.features.auth

data class LoginUIState(
    val phoneInput: String = "",
    val errorMessage: String? = null,
    val isEnglish: Boolean = true,
    val isLoading: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val verificationId: String? = null
)