package com.nabil.smartkrishi.domain.model

import com.nabil.smartkrishi.data.local.entity.UserEntity

/**
 * Domain Object representing a User in business logic.
 */
data class User(
    val id: Long = 0L,
    val serverId: String? = null,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val lastLogin: String? = null,
    val dateJoined: String? = null,
    val role: String,
    val profileImage: String? = null
)
/**
 * Mapper function to convert Domain User model to UserEntity.
 */
fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        serverId = serverId,
        firstName = firstName,
        lastName = lastName,
        phone = phone,
        lastLogin = lastLogin,
        dateJoined = dateJoined,
        role = role,
        profileImage = profileImage
    )
}
