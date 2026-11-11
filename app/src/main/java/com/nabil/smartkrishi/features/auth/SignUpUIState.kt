package com.nabil.smartkrishi.features.auth

/**
 * Data class representing the SignUp form field data.
 */
data class SignUpFormData(
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val nidNumber: String = "",
    val division: String = "",
    val district: String = "",
    val villageName: String = "",
    val role: String = "Farmer"
)

/**
 * Main UI State for the SignUp Screen managed by [SignUpViewModel].
 */
data class SignUpUIState(
    val formData: SignUpFormData = SignUpFormData(),
    val divisions: List<String> = emptyList(),
    val districts: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSignUpSuccess: Boolean = false
)
