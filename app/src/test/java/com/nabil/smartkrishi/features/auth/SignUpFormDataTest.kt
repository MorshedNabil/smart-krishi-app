package com.nabil.smartkrishi.features.auth

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit Test for [SignUpFormData].
 */
class SignUpFormDataTest {

    @Test
    fun signUpFormData_defaultValues_areCorrect() {
        val formData = SignUpFormData()

        assertEquals("", formData.firstName)
        assertEquals("", formData.lastName)
        assertEquals("", formData.phone)
        assertEquals("", formData.division)
        assertEquals("", formData.district)
        assertEquals("", formData.villageName)
        assertEquals("Farmer", formData.role)
    }

    @Test
    fun signUpFormData_customValues_areRetained() {
        val formData = SignUpFormData(
            firstName = "John",
            lastName = "Doe",
            phone = "1712345678",
            division = "Rangpur",
            district = "Panchagarh",
            villageName = "Rahitpur",
            role = "Vendor"
        )

        assertEquals("John", formData.firstName)
        assertEquals("Doe", formData.lastName)
        assertEquals("1712345678", formData.phone)
        assertEquals("Rangpur", formData.division)
        assertEquals("Panchagarh", formData.district)
        assertEquals("Rahitpur", formData.villageName)
        assertEquals("Vendor", formData.role)
    }
}
