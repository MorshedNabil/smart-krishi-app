package com.nabil.smartkrishi.features.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.nabil.smartkrishi.data.local.AppDatabase
import com.nabil.smartkrishi.data.repository.LocationRepository
import com.nabil.smartkrishi.data.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel managing UI state, validation, location syncing, and registration for [SignUpScreen].
 */
class SignUpViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val locationRepository: LocationRepository by lazy {
        LocationRepository(
            locationDao = AppDatabase.getInstance(application).locationDao()
        )
    }
    private val userRepository: UserRepository by lazy {
        UserRepository(
            userDao = AppDatabase.getInstance(application).userDao()
        )
    }

    private val _uiState = MutableStateFlow(SignUpUIState())
    val uiState: StateFlow<SignUpUIState> = _uiState.asStateFlow()

    private var districtFetchJob: Job? = null

    init {
        syncAndObserveLocations()
    }

    private fun syncAndObserveLocations() {
        try {
            // Sync locations from backend API into Room DB if Room DB is empty
            viewModelScope.launch {
                locationRepository.syncLocationsIfNeeded()
            }

            // Observe Division names dynamically from Room DB
            viewModelScope.launch {
                locationRepository.getDivisionNamesStream().collect { divisionList ->
                    _uiState.update { it.copy(divisions = divisionList) }
                }
            }
        } catch (e: Exception) {
            // Ignored in unit test environments without mocked Android framework
        }
    }

    fun updateFirstName(firstName: String) {
        _uiState.update {
            it.copy(
                formData = it.formData.copy(firstName = firstName),
                errorMessage = null
            )
        }
    }

    fun updateLastName(lastName: String) {
        _uiState.update {
            it.copy(
                formData = it.formData.copy(lastName = lastName),
                errorMessage = null
            )
        }
    }

    fun updatePhone(phone: String) {
        _uiState.update {
            it.copy(
                formData = it.formData.copy(phone = phone),
                errorMessage = null
            )
        }
    }

    fun updateDivision(division: String) {
        // Update selected division and reset selected district
        _uiState.update {
            it.copy(
                formData = it.formData.copy(division = division, district = ""),
                errorMessage = null
            )
        }

        // Dynamically fetch and observe District names for the selected division from Room DB
        districtFetchJob?.cancel()
        try {
            districtFetchJob = viewModelScope.launch {
                locationRepository.getDistrictNamesStream(division).collect { districtList ->
                    _uiState.update { it.copy(districts = districtList) }
                }
            }
        } catch (e: Exception) {
            // Ignored in unit tests
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

    fun validateAndSignUp(): Boolean {
        val formData = _uiState.value.formData

        if (formData.firstName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter First Name") }
            return false
        }
        if (formData.lastName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter Last Name") }
            return false
        }
        if (formData.phone.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter Phone Number") }
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

        return true
    }

    fun registerUser(onSuccess: () -> Unit = {}) {
        val formData = _uiState.value.formData

        if (!validateAndSignUp()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = userRepository.registerUser(
                firstName = formData.firstName,
                lastName = formData.lastName,
                phone = formData.phone,
                division = formData.division,
                district = formData.district,
                villageName = formData.villageName,
                role = formData.role
            )

            result.fold(
                onSuccess = { _ ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSignUpSuccess = true,
                            errorMessage = null
                        )
                    }
                    onSuccess()
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSignUpSuccess = false,
                            errorMessage = error.localizedMessage ?: "Registration failed. Please try again."
                        )
                    }
                }
            )
        }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
