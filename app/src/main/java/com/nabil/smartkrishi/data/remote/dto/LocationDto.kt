package com.nabil.smartkrishi.data.remote.dto

import com.nabil.smartkrishi.data.local.entity.DistrictEntity
import com.nabil.smartkrishi.data.local.entity.DivisionEntity
import com.nabil.smartkrishi.data.local.entity.VillageEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network DTO matching the nested location response structure from the backend API.
 */
@Serializable
data class DivisionDto(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("districts")
    val districts: List<DistrictDto>? = emptyList()
)

@Serializable
data class DistrictDto(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("villages")
    val villages: List<VillageDto>? = emptyList()
)

@Serializable
data class VillageDto(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String
)

// ============================================================================
// MAPPER FUNCTIONS: DTO -> Room Entity
// ============================================================================

fun DivisionDto.toEntity(): DivisionEntity {
    return DivisionEntity(
        id = id,
        name = name
    )
}

fun DistrictDto.toEntity(divisionId: Int, divisionName: String): DistrictEntity {
    return DistrictEntity(
        id = id,
        divisionId = divisionId,
        divisionName = divisionName,
        name = name
    )
}

fun VillageDto.toEntity(districtId: Int, districtName: String): VillageEntity {
    return VillageEntity(
        id = id,
        districtId = districtId,
        districtName = districtName,
        name = name
    )
}
