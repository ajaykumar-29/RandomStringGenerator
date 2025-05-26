package com.akumar.randomstringgenerator.ui.screens.randomStringScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akumar.randomstringgenerator.data.database.IRandomStringsDatabaseRepository
import com.akumar.randomstringgenerator.data.model.RandomStringItem
import com.akumar.randomstringgenerator.data.repository.IRandomStringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RandomStringViewModel @Inject constructor(
    private val randomStringRepository: IRandomStringRepository,
    private val randomStringsDatabaseRepository: IRandomStringsDatabaseRepository,
    @Named("IO")private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _result = MutableStateFlow<RandomStringFetchResult>(RandomStringFetchResult.None())
    val result: StateFlow<RandomStringFetchResult> = _result

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _inputValue = MutableStateFlow("")
    val inputValue: StateFlow<String> = _inputValue

    val randomStringList: StateFlow<List<RandomStringItem>> =
        randomStringsDatabaseRepository.randomStringsList
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                emptyList()
            )


    fun fetchRandomString(randomStringLength: Int) {
        _result.value = RandomStringFetchResult.Loading()
        viewModelScope.launch(ioDispatcher) {
            val result = randomStringRepository.getRandomString(randomStringLength)
            _result.value = result

            (result as? RandomStringFetchResult.Success)?.randomStringItem?.let { item ->
                randomStringsDatabaseRepository.insert(item)
                setInputValue("")
            }
        }
    }

    fun deleteAllStrings() {
        viewModelScope.launch(ioDispatcher) {
            randomStringsDatabaseRepository.deleteAllRandomStrings()
        }
    }

    fun deleteString(item: RandomStringItem) {
        viewModelScope.launch(ioDispatcher) {
            randomStringsDatabaseRepository.delete(item)
        }
    }

    fun setInputValue(newValue: String) {
        _inputValue.value = newValue
    }

    fun validateInput(input: String): Boolean {
        return when {
            input.isEmpty() -> {
                _errorMessage.value = "This field can not be empty."
                false
            }

            !input.all { it.isDigit() } -> {
                _errorMessage.value = "Invalid Input"
                false
            }

            else -> {
                _errorMessage.value = ""
                true
            }
        }
    }
}
