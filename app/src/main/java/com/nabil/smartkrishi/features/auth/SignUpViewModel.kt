package com.nabil.smartkrishi.features.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.nabil.smartkrishi.data.local.AppDatabase
import com.nabil.smartkrishi.data.repository.LocationRepository
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

    private val _uiState = MutableStateFlow(SignUpUIState())
    val uiState: StateFlow<SignUpUIState> = _uiState.asStateFlow()

    private var districtFetchJob: Job? = null

    init {
        syncAndObserveLocations()
    }

    private fun syncAndObserveLocations() {
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
    }

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
        // Update selected division and reset selected district
        _uiState.update {
            it.copy(
                formData = it.formData.copy(division = division, district = ""),
                errorMessage = null
            )
        }

        // Dynamically fetch and observe District names for the selected division from Room DB
        districtFetchJob?.cancel()
        districtFetchJob = viewModelScope.launch {
            locationRepository.getDistrictNamesStream(division).collect { districtList ->
                _uiState.update { it.copy(districts = districtList) }
            }
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
