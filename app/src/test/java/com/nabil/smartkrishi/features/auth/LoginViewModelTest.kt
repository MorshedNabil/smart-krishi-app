package com.nabil.smartkrishi.features.auth

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit Test for [LoginViewModel].
 */
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        viewModel = LoginViewModel()
    }

    @Test
    fun initialState_isCorrect() {
        val state = viewModel.uiState.value

        assertEquals("", state.phoneInput)
        assertNull(state.errorMessage)
        assertTrue(state.isEnglish)
        assertFalse(state.isLoading)
    }

    @Test
    fun onPhoneChange_updatesPhoneInputAndClearsError() {
        viewModel.onPhoneChange("1712345678")

        assertEquals("1712345678", viewModel.uiState.value.phoneInput)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun onLanguageToggle_updatesLanguageState() {
        viewModel.onLanguageToggle(false)

        assertFalse(viewModel.uiState.value.isEnglish)
    }
}
