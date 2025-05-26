package com.akumar.randomstringgenerator.ui.screens.randomStringScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akumar.randomstringgenerator.data.model.RandomStringItem
import com.akumar.randomstringgenerator.data.repository.IRandomStringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RandomStringViewModel @Inject constructor(
    private val repository: IRandomStringRepository
) : ViewModel() {

    private val _result = MutableStateFlow<RandomStringFetchResult>(RandomStringFetchResult.None())
    val result = _result.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _inputValue = MutableStateFlow("")
    val inputValue = _inputValue.asStateFlow()

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
                    setInputValue("")
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

    fun setInputValue(newValue: String) {
        _inputValue.value = newValue
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