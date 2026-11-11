package com.nabil.smartkrishi.data.remote.dto

import com.nabil.smartkrishi.data.local.entity.UserEntity
import com.nabil.smartkrishi.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network DTO representing the user profile response from GET /auth/accounts/me/
 */
@Serializable
data class UserResponseDto(
    @SerialName("user")
    val user: UserDto?
)

@Serializable
data class UserDto(
    @SerialName("id")
    val id: Int?,

    @SerialName("firebase_uid")
    val firebaseUid: String?,

    @SerialName("phone_number")
    val phoneNumber: String?,

    @SerialName("first_name")
    val firstName: String?,

    @SerialName("last_name")
    val lastName: String?,

    @SerialName("role")
    val role: String?,

    @SerialName("profile_image")
    val profileImage: String?,

    @SerialName("village_id")
    val villageId: Int?,

    @SerialName("created_at")
    val createdAt: String?,

    @SerialName("updated_at")
    val updatedAt: String?
)

/**
 * Mapper from UserDto to UserEntity.
 */
fun UserDto.toEntity(): UserEntity {
    val fName = firstName ?: ""
    val lName = lastName ?: ""

    return UserEntity(
        serverId = id,
        firstName = fName,
        lastName = lName,
        phone = phoneNumber ?: "",
        lastLogin = updatedAt,
        dateJoined = createdAt,
        role = role ?: "user",
        profileImage = profileImage,
        villageId = villageId
    )
}

/**
 * Mapper from UserDto to Domain Model User.
 */
fun UserDto.toDomain(): User {
    val fName = firstName ?: ""
    val lName = lastName ?: ""

    return User(
        serverId = id,
        firstName = fName,
        lastName = lName,
        phone = phoneNumber ?: "",
        lastLogin = updatedAt,
        dateJoined = createdAt,
        role = role ?: "user",
        profileImage = profileImage,
        villageId = villageId
    )
}
