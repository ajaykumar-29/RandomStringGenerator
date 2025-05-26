package com.akumar.randomstringgenerator.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.akumar.randomstringgenerator.data.model.RandomStringItem
import kotlinx.coroutines.flow.Flow

@Dao
interface IRandomStringDao {

    @Insert
    suspend fun insert(randomStringItem: RandomStringItem)

    @Delete
    suspend fun delete(randomStringItem: RandomStringItem)

    @Query("SELECT * FROM random_strings ORDER BY created DESC")
    fun getAllRandomStrings(): Flow<List<RandomStringItem>>

    @Query("DELETE FROM random_strings")
    suspend fun deleteAllRandomStrings()

}