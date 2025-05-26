package com.akumar.randomstringgenerator.di

import android.content.Context
import androidx.room.Room
import com.akumar.randomstringgenerator.data.database.IRandomStringDao
import com.akumar.randomstringgenerator.data.database.IRandomStringsDatabaseRepository
import com.akumar.randomstringgenerator.data.database.RandomStringsDatabase
import com.akumar.randomstringgenerator.data.database.RandomStringsDatabaseRepository
import com.akumar.randomstringgenerator.data.repository.IRandomStringRepository
import com.akumar.randomstringgenerator.data.repository.RandomStringRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRandomStringRepository(@ApplicationContext context: Context): IRandomStringRepository {
        return RandomStringRepository(context.contentResolver)
    }

    @Provides
    @Singleton
    fun provideRandomStringsDatabase(@ApplicationContext context: Context): RandomStringsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RandomStringsDatabase::class.java,
            "random_strings_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRandomStringDao(database: RandomStringsDatabase): IRandomStringDao {
        return database.getRandomStringDao()
    }

    @Provides
    @Singleton
    fun provideRandomStringsDatabaseRepository(randomStringDao: IRandomStringDao): IRandomStringsDatabaseRepository {
        return RandomStringsDatabaseRepository(randomStringDao)
    }
}