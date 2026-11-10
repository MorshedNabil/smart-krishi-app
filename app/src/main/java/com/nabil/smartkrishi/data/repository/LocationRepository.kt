package com.nabil.smartkrishi.data.repository

import android.util.Log
import com.nabil.smartkrishi.data.local.dao.LocationDao
import com.nabil.smartkrishi.data.local.entity.DistrictEntity
import com.nabil.smartkrishi.data.local.entity.DivisionEntity
import com.nabil.smartkrishi.data.local.entity.VillageEntity
import com.nabil.smartkrishi.data.remote.ApiService
import com.nabil.smartkrishi.data.remote.RetrofitClient
import com.nabil.smartkrishi.data.remote.dto.toEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository acting as Single Source of Truth (SSOT) for Location data (Divisions, Districts, Villages).
 */
class LocationRepository(
    private val locationDao: LocationDao,
    private val apiService: ApiService = RetrofitClient.apiService
) {

    /**
     * Observes list of all division names reactively from Room DB.
     */
    fun getDivisionNamesStream(): Flow<List<String>> {
        return locationDao.getAllDivisionNamesFlow()
    }

    /**
     * Observes list of district names for a specific division reactively from Room DB.
     */
    fun getDistrictNamesStream(divisionName: String): Flow<List<String>> {
        return locationDao.getDistrictNamesByDivisionFlow(divisionName)
    }

    /**
     * Observes list of village names for a specific district reactively from Room DB.
     */
    fun getVillageNamesStream(districtName: String): Flow<List<String>> {
        return locationDao.getVillageNamesByDistrictFlow(districtName)
    }

    /**
     * Checks if location data is present in Room DB. If empty, fetches the nested
     * location hierarchy from backend API, flattens it, and populates Room DB.
     */
    suspend fun syncLocationsIfNeeded(): Result<Unit> {
        return runCatching {
            val divisionsCount = locationDao.getDivisionsCount()
            if (divisionsCount == 0) {
                Log.d("LocationRepository", "Room DB locations empty. Fetching locations from API...")
                val divisionsList = apiService.getLocations()

                val divisionEntities = mutableListOf<DivisionEntity>()
                val districtEntities = mutableListOf<DistrictEntity>()
                val villageEntities = mutableListOf<VillageEntity>()

                divisionsList.forEach { divisionDto ->
                    divisionEntities.add(divisionDto.toEntity())
                    divisionDto.districts?.forEach { districtDto ->
                        districtEntities.add(
                            districtDto.toEntity(
                                divisionId = divisionDto.id,
                                divisionName = divisionDto.name
                            )
                        )
                        districtDto.villages?.forEach { villageDto ->
                            villageEntities.add(
                                villageDto.toEntity(
                                    districtId = districtDto.id,
                                    districtName = districtDto.name
                                )
                            )
                        }
                    }
                }

                locationDao.insertDivisions(divisionEntities)
                locationDao.insertDistricts(districtEntities)
                locationDao.insertVillages(villageEntities)

                Log.d("LocationRepository", "Successfully synced ${divisionEntities.size} divisions, ${districtEntities.size} districts, and ${villageEntities.size} villages into Room DB.")
            } else {
                Log.d("LocationRepository", "Locations already present in Room DB ($divisionsCount divisions).")
            }
            Unit
        }.onFailure { exception ->
            Log.e("LocationRepository", "Failed to sync locations from API", exception)
        }
    }
}
