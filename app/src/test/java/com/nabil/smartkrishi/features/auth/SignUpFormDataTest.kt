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

        assertEquals("", formData.phone)
        assertEquals("", formData.nidNumber)
        assertEquals("", formData.division)
        assertEquals("", formData.district)
        assertEquals("", formData.villageName)
        assertEquals("Farmer", formData.role)
    }

    @Test
    fun signUpFormData_customValues_areRetained() {
        val formData = SignUpFormData(
            phone = "1712345678",
            nidNumber = "9876543210",
            division = "Rangpur",
            district = "Panchagarh",
            villageName = "Rahitpur",
            role = "Vendor"
        )

        assertEquals("1712345678", formData.phone)
        assertEquals("9876543210", formData.nidNumber)
        assertEquals("Rangpur", formData.division)
        assertEquals("Panchagarh", formData.district)
        assertEquals("Rahitpur", formData.villageName)
        assertEquals("Vendor", formData.role)
    }
}
