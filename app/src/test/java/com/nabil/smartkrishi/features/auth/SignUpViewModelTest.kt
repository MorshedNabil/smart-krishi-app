package com.nabil.smartkrishi.features.auth

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
        viewModel = SignUpViewModel()
    }

    @Test
    fun initialState_hasDefaultFormData() {
        val state = viewModel.uiState.value

        assertEquals("", state.formData.phone)
        assertEquals("", state.formData.nidNumber)
        assertEquals("", state.formData.division)
        assertEquals("", state.formData.district)
        assertEquals("", state.formData.villageName)
        assertEquals("Farmer", state.formData.role)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun validateAndSignUp_whenFieldsAreBlank_returnsFalseAndSetsErrorMessage() {
        // Phone is blank
        val isPhoneValid = viewModel.validateAndSignUp()
        assertFalse(isPhoneValid)
        assertEquals("Please enter Phone Number", viewModel.uiState.value.errorMessage)

        // Fill phone, NID is blank
        viewModel.updatePhone("1712345678")
        val isNidValid = viewModel.validateAndSignUp()
        assertFalse(isNidValid)
        assertEquals("Please enter NID number", viewModel.uiState.value.errorMessage)

        // Fill NID, Division is blank
        viewModel.updateNid("1234567890")
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
        viewModel.updatePhone("1712345678")
        viewModel.updateNid("1234567890")
        viewModel.updateDivision("Dhaka")
        viewModel.updateDistrict("Chittagong")
        viewModel.updateVillage("Rahitpur")
        viewModel.updateRole("Vendor")

        val isValid = viewModel.validateAndSignUp()

        assertTrue(isValid)
        assertTrue(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.errorMessage)
        assertEquals("1712345678", viewModel.uiState.value.formData.phone)
        assertEquals("1234567890", viewModel.uiState.value.formData.nidNumber)
        assertEquals("Dhaka", viewModel.uiState.value.formData.division)
        assertEquals("Chittagong", viewModel.uiState.value.formData.district)
        assertEquals("Rahitpur", viewModel.uiState.value.formData.villageName)
        assertEquals("Vendor", viewModel.uiState.value.formData.role)
    }
}
