package com.nabil.smartkrishi.features.auth

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nabil.smartkrishi.ui.theme.SmartKrishiTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Compose UI Instrumented Test for [SignUpScreen] and [SignUpContent].
 */
@RunWith(AndroidJUnit4::class)
class SignUpScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun signUpScreen_displaysAllLabelsAndElements() {
        composeTestRule.setContent {
            SmartKrishiTheme {
                SignUpContent(uiState = SignUpUIState())
            }
        }

        // Verify "Sign up" appears twice (Screen Title + Submit Button)
        composeTestRule.onAllNodesWithText("Sign up").assertCountEquals(2)

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
    fun signUpContent_whenSignUpClicked_triggersCallbackWithCorrectData() {
        var submittedData: SignUpFormData? = null

        val testFormData = SignUpFormData(
            phone = "1712345678",
            nidNumber = "1234567890",
            division = "Dhaka",
            district = "Chittagong",
            villageName = "Rahitpur",
            role = "Vendor"
        )

        composeTestRule.setContent {
            SmartKrishiTheme {
                SignUpContent(
                    uiState = SignUpUIState(formData = testFormData),
                    onSignUpClick = {
                        submittedData = testFormData
                    }
                )
            }
        }

        // Click the clickable "Sign up" button
        composeTestRule.onNode(hasText("Sign up") and hasClickAction()).performClick()

        // Verify Submitted Form Data
        assertNotNull("Submitted form data should not be null", submittedData)
        assertEquals("1712345678", submittedData?.phone)
        assertEquals("1234567890", submittedData?.nidNumber)
        assertEquals("Dhaka", submittedData?.division)
        assertEquals("Chittagong", submittedData?.district)
        assertEquals("Rahitpur", submittedData?.villageName)
        assertEquals("Vendor", submittedData?.role)
    }
}
