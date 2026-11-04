package com.nabil.smartkrishi.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nabil.smartkrishi.data.local.entity.DistrictEntity
import com.nabil.smartkrishi.data.local.entity.DivisionEntity
import com.nabil.smartkrishi.data.local.entity.VillageEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room Data Access Object (DAO) for managing Divisions, Districts, and Villages caching.
 */
@Dao
interface LocationDao {

    // ================= Divisions =================
    @Query("SELECT * FROM divisions ORDER BY name ASC")
    fun getAllDivisionsFlow(): Flow<List<DivisionEntity>>

    @Query("SELECT name FROM divisions ORDER BY name ASC")
    fun getAllDivisionNamesFlow(): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM divisions")
    suspend fun getDivisionsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDivisions(divisions: List<DivisionEntity>)

    // ================= Districts =================
    @Query("SELECT * FROM districts ORDER BY name ASC")
    fun getAllDistrictsFlow(): Flow<List<DistrictEntity>>

    @Query("SELECT name FROM districts WHERE divisionName = :divisionName ORDER BY name ASC")
    fun getDistrictNamesByDivisionFlow(divisionName: String): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM districts")
    suspend fun getDistrictsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistricts(districts: List<DistrictEntity>)

    // ================= Villages =================
    @Query("SELECT name FROM villages WHERE districtName = :districtName ORDER BY name ASC")
    fun getVillageNamesByDistrictFlow(districtName: String): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM villages")
    suspend fun getVillagesCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVillages(villages: List<VillageEntity>)

    // ================= Clear All =================
    @Query("DELETE FROM divisions")
    suspend fun clearDivisions()

    @Query("DELETE FROM districts")
    suspend fun clearDistricts()

    @Query("DELETE FROM villages")
    suspend fun clearVillages()
}
