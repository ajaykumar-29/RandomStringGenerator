package com.akumar.randomstringgenerator.data.repository

import com.akumar.randomstringgenerator.ui.screens.randomStringScreen.RandomStringFetchResult

interface IRandomStringRepository {
    fun getRandomString(randomStringLength: Int): RandomStringFetchResult
}