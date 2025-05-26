package com.akumar.randomstringgenerator.data.database

import androidx.annotation.WorkerThread
import com.akumar.randomstringgenerator.data.model.RandomStringItem
import kotlinx.coroutines.flow.Flow

class RandomStringsDatabaseRepository(private val randomStringDao: IRandomStringDao) {
    val randomStringsList: Flow<List<RandomStringItem>> = randomStringDao.getAllRandomStrings()

    @WorkerThread
    suspend fun insert(randomStringItem: RandomStringItem) =
        randomStringDao.insert(randomStringItem)

    @WorkerThread
    suspend fun delete(randomStringItem: RandomStringItem) =
        randomStringDao.delete(randomStringItem)

    @WorkerThread
    suspend fun deleteAllRandomStrings() = randomStringDao.deleteAllRandomStrings()

}