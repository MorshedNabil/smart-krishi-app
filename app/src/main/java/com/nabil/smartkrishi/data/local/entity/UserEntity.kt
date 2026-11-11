package com.nabil.smartkrishi.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nabil.smartkrishi.domain.model.User
import kotlinx.serialization.Serializable

/**
 * Room Entity representing a User in the local database.
 */
@Entity(tableName = "users")
@Serializable
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "server_id")
    val serverId: String? = null,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    @ColumnInfo(name = "phone")
    val phone: String,

    @ColumnInfo(name = "last_login")
    val lastLogin: String? = null,

    @ColumnInfo(name = "date_joined")
    val dateJoined: String? = null,

    @ColumnInfo(name = "role")
    val role: String,

    @ColumnInfo(name = "profile_image")
    val profileImage: String? = null
)

/**
 * Mapper function to convert UserEntity to Domain User model.
 */
fun UserEntity.toDomain(): User {
    return User(
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
