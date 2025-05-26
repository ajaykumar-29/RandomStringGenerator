package com.akumar.randomstringgenerator.data.database

import androidx.annotation.WorkerThread
import com.akumar.randomstringgenerator.data.model.RandomStringItem
import kotlinx.coroutines.flow.Flow

class RandomStringsDatabaseRepository(
    private val randomStringDao: IRandomStringDao
) : IRandomStringsDatabaseRepository {
    override val randomStringsList: Flow<List<RandomStringItem>> =
        randomStringDao.getAllRandomStrings()

    @WorkerThread
    override suspend fun insert(randomStringItem: RandomStringItem) =
        randomStringDao.insert(randomStringItem)

    @WorkerThread
    override suspend fun delete(randomStringItem: RandomStringItem) =
        randomStringDao.delete(randomStringItem)

    @WorkerThread
    override suspend fun deleteAllRandomStrings() = randomStringDao.deleteAllRandomStrings()

}