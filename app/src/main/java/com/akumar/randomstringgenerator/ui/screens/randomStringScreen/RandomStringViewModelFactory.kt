package com.akumar.randomstringgenerator.ui.screens.randomStringScreen

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.akumar.randomstringgenerator.data.repository.RandomStringRepository

class RandomStringViewModelFactory(
    contentResolver: ContentResolver,
) : ViewModelProvider.Factory {
    private val repository: RandomStringRepository =
        RandomStringRepository(contentResolver)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RandomStringViewModel::class.java)) {
            return RandomStringViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}