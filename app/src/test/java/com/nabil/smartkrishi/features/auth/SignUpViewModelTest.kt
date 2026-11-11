package com.nabil.smartkrishi.features.auth

import android.app.Application
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit Test for [SignUpViewModel].
 */
class SignUpViewModelTest {

    private lateinit var viewModel: SignUpViewModel

    @Before
    fun setUp() {
        val application = Application()
        viewModel = SignUpViewModel(application)
    }

    @Test
    fun initialState_hasDefaultFormData() {
        val state = viewModel.uiState.value

        assertEquals("", state.formData.firstName)
        assertEquals("", state.formData.lastName)
        assertEquals("", state.formData.phone)
        assertEquals("", state.formData.division)
        assertEquals("", state.formData.district)
        assertEquals("", state.formData.villageName)
        assertEquals("Farmer", state.formData.role)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun validateAndSignUp_whenFieldsAreBlank_returnsFalseAndSetsErrorMessage() {
        // First Name is blank
        val isFirstNameValid = viewModel.validateAndSignUp()
        assertFalse(isFirstNameValid)
        assertEquals("Please enter First Name", viewModel.uiState.value.errorMessage)

        // Fill First Name, Last Name is blank
        viewModel.updateFirstName("John")
        val isLastNameValid = viewModel.validateAndSignUp()
        assertFalse(isLastNameValid)
        assertEquals("Please enter Last Name", viewModel.uiState.value.errorMessage)

        // Fill Last Name, Phone is blank
        viewModel.updateLastName("Doe")
        val isPhoneValid = viewModel.validateAndSignUp()
        assertFalse(isPhoneValid)
        assertEquals("Please enter Phone Number", viewModel.uiState.value.errorMessage)

        // Fill Phone, Division is blank
        viewModel.updatePhone("1712345678")
        val isDivisionValid = viewModel.validateAndSignUp()
        assertFalse(isDivisionValid)
        assertEquals("Please Select your Division", viewModel.uiState.value.errorMessage)

        // Fill Division, District is blank
        viewModel.updateDivision("Dhaka")
        val isDistrictValid = viewModel.validateAndSignUp()
        assertFalse(isDistrictValid)
        assertEquals("Please Select your District", viewModel.uiState.value.errorMessage)

        // Fill District, Village is blank
        viewModel.updateDistrict("Chittagong")
        val isVillageValid = viewModel.validateAndSignUp()
        assertFalse(isVillageValid)
        assertEquals("Please enter Village name", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun validateAndSignUp_whenAllFieldsAreFilled_returnsTrue() {
        viewModel.updateFirstName("John")
        viewModel.updateLastName("Doe")
        viewModel.updatePhone("1712345678")
        viewModel.updateDivision("Dhaka")
        viewModel.updateDistrict("Chittagong")
        viewModel.updateVillage("Rahitpur")
        viewModel.updateRole("Vendor")

        val isValid = viewModel.validateAndSignUp()

        assertTrue(isValid)
        assertTrue(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.errorMessage)
        assertEquals("John", viewModel.uiState.value.formData.firstName)
        assertEquals("Doe", viewModel.uiState.value.formData.lastName)
        assertEquals("1712345678", viewModel.uiState.value.formData.phone)
        assertEquals("Dhaka", viewModel.uiState.value.formData.division)
        assertEquals("Chittagong", viewModel.uiState.value.formData.district)
        assertEquals("Rahitpur", viewModel.uiState.value.formData.villageName)
        assertEquals("Vendor", viewModel.uiState.value.formData.role)
    }
}
