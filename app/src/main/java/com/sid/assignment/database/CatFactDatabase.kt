package com.sid.assignment.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [CatFact::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(ImgConverters::class)
abstract class CatFactDatabase : RoomDatabase() {

    abstract fun catFactDao(): CatFactDao

    companion object {

        @Volatile
        private var INSTANCE: CatFactDatabase? = null

        fun getDatabase(context: Context): CatFactDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {
                    // Pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context)
                }
            }
            // Return database.
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): CatFactDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                CatFactDatabase::class.java,
                "catfact_database"
            )
                .build()
        }
    }
}
