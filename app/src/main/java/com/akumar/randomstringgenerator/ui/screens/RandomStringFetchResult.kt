package com.akumar.randomstringgenerator.ui.screens

import com.akumar.randomstringgenerator.data.model.RandomStringItem

sealed class RandomStringFetchResult(
    val randomStringItem: RandomStringItem? = null,
    val message: String? = null
) {
    class Success(randomStringItem: RandomStringItem) : RandomStringFetchResult(randomStringItem)
    class Error(message: String?) : RandomStringFetchResult(message = message)
    class Loading : RandomStringFetchResult()
    class None : RandomStringFetchResult()
}