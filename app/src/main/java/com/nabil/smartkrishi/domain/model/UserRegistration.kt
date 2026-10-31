package com.nabil.smartkrishi.domain.model

/**
 * Domain Object (DO) representing user registration details in business logic.
 */
data class UserRegistration(
    val phone: String,
    val nidNumber: String,
    val division: String,
    val district: String,
    val villageName: String,
    val role: String
)
