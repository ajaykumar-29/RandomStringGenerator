package com.akumar.randomstringgenerator.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akumar.randomstringgenerator.data.model.RandomStringItem

@Database(entities = [RandomStringItem::class], exportSchema = false, version = 1)
abstract class RandomStringsDatabase : RoomDatabase() {
    abstract fun getRandomStringDao(): IRandomStringDao
}