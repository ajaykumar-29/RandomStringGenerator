package com.akumar.randomstringgenerator.repository

import com.akumar.randomstringgenerator.ui.screens.RandomStringFetchResult

interface IRandomStringRepository {
    fun getRandomString(randomStringLength: Int): RandomStringFetchResult
}