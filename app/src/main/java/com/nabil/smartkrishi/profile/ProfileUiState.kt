package com.nabil.smartkrishi.profile

import android.net.Uri

data class ProfileUiState (
    val name: String = "",
    val phoneNumber: String = "",
    val selectedLocation: String = "",
    val profileImageUri: Uri? = null,
    val isLocationDropdownExpanded: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSaveSuccess: Boolean = false
)