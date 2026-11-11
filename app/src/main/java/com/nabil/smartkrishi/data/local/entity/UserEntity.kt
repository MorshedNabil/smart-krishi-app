package com.nabil.smartkrishi.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nabil.smartkrishi.domain.model.User
import kotlinx.serialization.Serializable

/**
 * Room Entity representing a User in the local database.
 */
@Entity(
    tableName = "users",
    foreignKeys = [
        ForeignKey(
            entity = VillageEntity::class,
            parentColumns = ["id"],
            childColumns = ["village_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["village_id"])]
)
@Serializable
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "server_id")
    val serverId: Int? = null,

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
    val profileImage: String? = null,

    @ColumnInfo(name = "village_id")
    val villageId: Int? = null
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
        profileImage = profileImage,
        villageId = villageId
    )
}
