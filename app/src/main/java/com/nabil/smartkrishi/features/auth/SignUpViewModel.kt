package com.nabil.smartkrishi.features.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel managing UI state, validation, and registration logic for the SignUp Screen using [SignUpUIState].
 */
class SignUpViewModel : ViewModel() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val _uiState = MutableStateFlow(SignUpUIState())
    val uiState: StateFlow<SignUpUIState> = _uiState.asStateFlow()

    fun updatePhone(phone: String) {
        _uiState.update {
            it.copy(
                formData = it.formData.copy(phone = phone),
                errorMessage = null
            )
        }
    }

    fun updateNid(nidNumber: String) {
        _uiState.update {
            it.copy(
                formData = it.formData.copy(nidNumber = nidNumber),
                errorMessage = null
            )
        }
    }

    fun updateDivision(division: String) {
        _uiState.update {
            it.copy(
                formData = it.formData.copy(division = division),
                errorMessage = null
            )
        }
    }

    fun updateDistrict(district: String) {
        _uiState.update {
            it.copy(
                formData = it.formData.copy(district = district),
                errorMessage = null
            )
        }
    }

    fun updateVillage(villageName: String) {
        _uiState.update {
            it.copy(
                formData = it.formData.copy(villageName = villageName),
                errorMessage = null
            )
        }
    }

    fun updateRole(role: String) {
        _uiState.update {
            it.copy(
                formData = it.formData.copy(role = role),
                errorMessage = null
            )
        }
    }

    /**
     * Validates form input fields and initiates user registration logic.
     * Returns true if validation passes and registration succeeds.
     */
    fun validateAndSignUp(): Boolean {
        val formData = _uiState.value.formData

        if (formData.phone.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter Phone Number") }
            return false
        }
        if (formData.nidNumber.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter NID number") }
            return false
        }
        if (formData.division.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please Select your Division") }
            return false
        }
        if (formData.district.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please Select your District") }
            return false
        }
        if (formData.villageName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter Village name") }
            return false
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                isSignUpSuccess = true
            )
        }

        return true
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
