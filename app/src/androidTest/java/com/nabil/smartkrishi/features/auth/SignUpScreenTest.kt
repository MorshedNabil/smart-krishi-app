package com.nabil.smartkrishi.features.auth

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nabil.smartkrishi.ui.theme.SmartKrishiTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Compose UI Instrumented Test for [SignUpScreen].
 */
@RunWith(AndroidJUnit4::class)
class SignUpScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun signUpScreen_displaysAllLabelsAndElements() {
        composeTestRule.setContent {
            SmartKrishiTheme {
                SignUpScreen()
            }
        }

        // Verify main Title
        composeTestRule.onNodeWithText("Sign up").assertIsDisplayed()

        // Verify Form Labels
        composeTestRule.onNodeWithText("Phone").assertIsDisplayed()
        composeTestRule.onNodeWithText("Nid").assertIsDisplayed()
        composeTestRule.onNodeWithText("Division").assertIsDisplayed()
        composeTestRule.onNodeWithText("District").assertIsDisplayed()
        composeTestRule.onNodeWithText("Village Name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign up as").assertIsDisplayed()

        // Verify Role Options
        composeTestRule.onNodeWithText("Farmer").assertIsDisplayed()
        composeTestRule.onNodeWithText("Vendor").assertIsDisplayed()
        composeTestRule.onNodeWithText("Buyer").assertIsDisplayed()
    }

    @Test
    fun signUpScreen_userFillsForm_triggersOnSignUpClickWithCorrectData() {
        var submittedData: SignUpFormData? = null

        composeTestRule.setContent {
            SmartKrishiTheme {
                SignUpScreen(
                    onSignUpClick = { data ->
                        submittedData = data
                    }
                )
            }
        }

        // Input NID Number
        composeTestRule.onNodeWithText("Enter NID number").performTextReplacement("1234567890")

        // Input Village Name
        composeTestRule.onNodeWithText("Enter village name").performTextReplacement("Rahitpur")

        // Select Role: "Vendor"
        composeTestRule.onNodeWithText("Vendor").performClick()

        // Click "Sign up" button
        composeTestRule.onNodeWithText("Sign up").performClick()

        // Verify Submitted Form Data
        assertNotNull(submittedData)
        assertEquals("1234567890", submittedData?.nidNumber)
        assertEquals("Rahitpur", submittedData?.villageName)
        assertEquals("Vendor", submittedData?.role)
    }
}
