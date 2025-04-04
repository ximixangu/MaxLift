package com.maxlift.data.datasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.maxlift.data.model.database.ExerciseEntity
import com.maxlift.data.model.database.PersonEntity

@Database(entities = [PersonEntity::class, ExerciseEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDataSource(): PersonDao
    abstract fun exerciseDataSource(): ExerciseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "maxLift_database:2.0"
                )
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}