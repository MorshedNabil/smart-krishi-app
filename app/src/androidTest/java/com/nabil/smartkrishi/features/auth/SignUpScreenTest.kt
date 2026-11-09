package com.nabil.smartkrishi.features.auth

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
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
    fun signUpScreen_divisionAndDistrictDropdowns_displayOptionsAndTriggerCallbacks() {
        var selectedDivision = ""
        var selectedDistrict = ""

        val testDivisions = listOf("Barishal", "Dhaka", "Rangpur")
        val testDistricts = listOf("Panchagarh", "Dinajpur")

        composeTestRule.setContent {
            SmartKrishiTheme {
                SignUpContent(
                    uiState = SignUpUIState(
                        divisions = testDivisions,
                        districts = testDistricts,
                        formData = SignUpFormData(
                            division = selectedDivision,
                            district = selectedDistrict
                        )
                    ),
                    divisions = testDivisions,
                    districts = testDistricts,
                    onDivisionSelected = { selectedDivision = it },
                    onDistrictSelected = { selectedDistrict = it }
                )
            }
        }

        // Test Division Dropdown: Click field, verify option displays, select option
        composeTestRule.onNodeWithText("Select Division").performClick()
        composeTestRule.onNodeWithText("Rangpur").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rangpur").performClick()
        assertEquals("Rangpur", selectedDivision)

        // Test District Dropdown: Click field, verify option displays, select option
        composeTestRule.onNodeWithText("Select District").performClick()
        composeTestRule.onNodeWithText("Panchagarh").assertIsDisplayed()
        composeTestRule.onNodeWithText("Panchagarh").performClick()
        assertEquals("Panchagarh", selectedDistrict)
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

        // Scroll to the SignUp button inside the scrollable column, then perform click
        composeTestRule.onNodeWithTag("btn_signup")
            .performScrollTo()
            .performClick()

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
