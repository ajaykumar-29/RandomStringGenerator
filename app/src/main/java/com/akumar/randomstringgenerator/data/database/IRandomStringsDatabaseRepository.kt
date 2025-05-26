package com.akumar.randomstringgenerator.data.database

import com.akumar.randomstringgenerator.data.model.RandomStringItem
import kotlinx.coroutines.flow.Flow

interface IRandomStringsDatabaseRepository {
    val randomStringsList: Flow<List<RandomStringItem>>
    suspend fun insert(randomStringItem: RandomStringItem)
    suspend fun delete(randomStringItem: RandomStringItem)
    suspend fun deleteAllRandomStrings()
}