package com.akumar.randomstringgenerator.di

import android.content.Context
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
}