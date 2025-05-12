package com.akumar.randomstringgenerator.ui.screens

import android.app.Application
import android.content.ContentResolver
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.akumar.randomstringgenerator.data.model.RandomStringItem
import com.akumar.randomstringgenerator.repository.IRandomStringRepository
import com.akumar.randomstringgenerator.repository.RandomStringRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RandomStringViewModel(
    private val repository: IRandomStringRepository
) : ViewModel() {

    private val _result = MutableStateFlow<RandomStringFetchResult>(RandomStringFetchResult.None())
    val result = _result.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _randomStringList = MutableStateFlow<List<RandomStringItem>>(emptyList())
    val randomStringList = _randomStringList.asStateFlow()

    fun fetchRandomString(randomStringLength: Int) {
        _result.value = RandomStringFetchResult.Loading()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _result.value = repository.getRandomString(randomStringLength)
                if (_result.value is RandomStringFetchResult.Success) {
                    _result.value.randomStringItem?.let {
                        _randomStringList.value = listOf(it) + _randomStringList.value
                    }
                }
            }
        }
    }

    fun deleteAllStrings() {
        _randomStringList.value = emptyList()
    }

    fun deleteString(item: RandomStringItem) {
        _randomStringList.value = _randomStringList.value.filterNot { it == item }
    }

    fun validateInput(input: String): Boolean {
        if (input.isEmpty()) {
            _errorMessage.value = "This field can not be empty."
            return false
        } else if (!input.all { it.isDigit() }) {
            _errorMessage.value = "Invalid Input"
            return false
        }
        _errorMessage.value = ""
        return true
    }
}


class RandomStringViewModelFactory(
    private val contentResolver: ContentResolver,
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


