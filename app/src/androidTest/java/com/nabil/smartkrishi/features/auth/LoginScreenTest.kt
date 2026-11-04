package com.nabil.smartkrishi.features.auth

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nabil.smartkrishi.ui.theme.SmartKrishiTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Compose UI Instrumented Test for [LoginScreen] and [LoginContent].
 */
@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loginScreen_displaysAllElements() {
        composeTestRule.setContent {
            SmartKrishiTheme {
                LoginContent(uiState = LoginUIState())
            }
        }

        // Verify Title and Header Elements
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
        composeTestRule.onNodeWithText("EN").assertIsDisplayed()
        composeTestRule.onNodeWithText("Phone").assertIsDisplayed()
        composeTestRule.onNodeWithText("Get OTP").assertIsDisplayed()
        composeTestRule.onNodeWithText("Don't have an account? Sign up").assertIsDisplayed()
    }

    @Test
    fun loginScreen_whenLoading_buttonIsDisabled() {
        composeTestRule.setContent {
            SmartKrishiTheme {
                LoginContent(uiState = LoginUIState(isLoading = true))
            }
        }

        // Verify Get OTP button is disabled during loading
        composeTestRule.onNodeWithTag("btn_get_otp").assertIsNotEnabled()
    }

    @Test
    fun loginScreen_whenErrorMessagePresent_displaysError() {
        val testErrorMessage = "Please enter phone number"

        composeTestRule.setContent {
            SmartKrishiTheme {
                LoginContent(uiState = LoginUIState(errorMessage = testErrorMessage))
            }
        }

        // Verify error message text is displayed
        composeTestRule.onNodeWithText(testErrorMessage).assertIsDisplayed()
    }

    @Test
    fun loginScreen_userTypesPhoneAndClicksGetOtp_triggersCallback() {
        var isGetOtpClicked = false
        var enteredPhone = ""

        composeTestRule.setContent {
            SmartKrishiTheme {
                LoginContent(
                    uiState = LoginUIState(phoneInput = enteredPhone),
                    onPhoneChange = { enteredPhone = it },
                    onGetOtpClick = { isGetOtpClicked = true }
                )
            }
        }

        // Type phone number into input field
        composeTestRule.onNode(hasSetTextAction()).performTextReplacement("1712345678")

        // Click Get OTP button
        composeTestRule.onNodeWithTag("btn_get_otp").performClick()

        // Verify callbacks
        assertTrue("Get OTP button callback should be triggered", isGetOtpClicked)
        assertEquals("1712345678", enteredPhone)
    }

    @Test
    fun loginScreen_whenSignUpClicked_triggersSignUpCallback() {
        var isSignUpClicked = false

        composeTestRule.setContent {
            SmartKrishiTheme {
                LoginContent(
                    uiState = LoginUIState(),
                    onSignUpClick = { isSignUpClicked = true }
                )
            }
        }

        // Click Sign Up link
        composeTestRule.onNodeWithText("Don't have an account? Sign up").performClick()

        // Verify callback
        assertTrue("SignUp link callback should be triggered", isSignUpClicked)
    }
}
