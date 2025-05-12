package com.akumar.randomstringgenerator

import com.akumar.randomstringgenerator.repository.IRandomStringRepository
import com.akumar.randomstringgenerator.ui.screens.RandomStringFetchResult

class FakeRandomStringRepository : IRandomStringRepository {
    private var result: RandomStringFetchResult = RandomStringFetchResult.None()
    fun setResult(fetchResult: RandomStringFetchResult) {
        result = fetchResult
    }

    override fun getRandomString(randomStringLength: Int): RandomStringFetchResult {
        return result
    }
}