package com.nabil.smartkrishi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nabil.smartkrishi.data.local.converters.WeatherConverters
import com.nabil.smartkrishi.data.local.dao.LocationDao
import com.nabil.smartkrishi.data.local.dao.WeatherDao
import com.nabil.smartkrishi.data.local.entity.DistrictEntity
import com.nabil.smartkrishi.data.local.entity.DivisionEntity
import com.nabil.smartkrishi.data.local.entity.VillageEntity
import com.nabil.smartkrishi.data.local.entity.WeatherEntity

/**
 * Main Room Database configuration for Smart Krishi.
 */
@Database(
    entities = [
        WeatherEntity::class,
        DivisionEntity::class,
        DistrictEntity::class,
        VillageEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(WeatherConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
    abstract fun locationDao(): LocationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_krishi_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
