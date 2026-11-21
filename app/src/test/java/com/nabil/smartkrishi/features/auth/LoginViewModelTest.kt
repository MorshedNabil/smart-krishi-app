package com.nabil.smartkrishi.features.auth

import android.app.Application
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
        val application = Application()
        viewModel = LoginViewModel(application)
    }

    @Test
    fun initialState_isCorrect() {
        val state = viewModel.uiState.value

        assertEquals("", state.phoneInput)
        assertEquals("", state.passwordInput)
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
    fun onPasswordChange_updatesPasswordInputAndClearsError() {
        viewModel.onPasswordChange("123456")

        assertEquals("123456", viewModel.uiState.value.passwordInput)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun onLanguageToggle_updatesLanguageState() {
        viewModel.onLanguageToggle(false)

        assertFalse(viewModel.uiState.value.isEnglish)
    }
}
