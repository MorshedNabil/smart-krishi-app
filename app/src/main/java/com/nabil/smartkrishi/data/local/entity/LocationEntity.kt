package com.nabil.smartkrishi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Room Entity representing a Division in Bangladesh.
 */
@Entity(tableName = "divisions")
@Serializable
data class DivisionEntity(
    @PrimaryKey
    val id: Int,
    val name: String
)

/**
 * Room Entity representing a District within a Division.
 */
@Entity(tableName = "districts")
@Serializable
data class DistrictEntity(
    @PrimaryKey
    val id: Int,
    val divisionId: Int,
    val divisionName: String,
    val name: String
)

/**
 * Room Entity representing a Village within a District.
 */
@Entity(tableName = "villages")
@Serializable
data class VillageEntity(
    @PrimaryKey
    val id: Int,
    val districtId: Int,
    val districtName: String,
    val name: String
)
