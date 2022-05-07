package com.sid.assignment.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CatFactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCatFact(catfact: CatFact)

    @Query("SELECT * FROM catfacts ORDER BY id ASC")
    fun getCatFact(): Flow<CatFact>

    @Delete
    suspend fun deleteCatFact(catfact: CatFact)

}
