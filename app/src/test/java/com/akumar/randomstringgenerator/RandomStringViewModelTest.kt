package com.akumar.randomstringgenerator

import android.app.Application
import com.akumar.randomstringgenerator.data.model.RandomStringItem
import com.akumar.randomstringgenerator.ui.screens.RandomStringFetchResult
import com.akumar.randomstringgenerator.ui.screens.RandomStringViewModel
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class RandomStringViewModelTest {

    private lateinit var viewModel: RandomStringViewModel
    private lateinit var fakeRepository: FakeRandomStringRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        fakeRepository = FakeRandomStringRepository()
        viewModel = RandomStringViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `deleteAllStrings should clear the randomStringList`() = runTest {
        val randomStringItem = RandomStringItem("test", 4, "01 January, 2023 10:00")
        fakeRepository.setResult(RandomStringFetchResult.Success(randomStringItem))

        viewModel.fetchRandomString(5)
        advanceUntilIdle()
        viewModel.deleteAllStrings()

        assertTrue(viewModel.randomStringList.value.isEmpty())
    }

    @Test
    fun `deleteString should remove the specific item from randomStringList`() = runTest {
        val item1 = RandomStringItem("test1", 5, "01 January, 2023 10:00")
        val item2 = RandomStringItem("test2", 6, "02 January, 2023 11:00")

        fakeRepository.setResult(RandomStringFetchResult.Success(item1))
        viewModel.fetchRandomString(5)
        advanceUntilIdle()

        fakeRepository.setResult(RandomStringFetchResult.Success(item2))
        viewModel.fetchRandomString(6)
        advanceUntilIdle()

        viewModel.deleteString(item1)

        assertFalse(viewModel.randomStringList.value.contains(item1))
        assertTrue(viewModel.randomStringList.value.contains(item2))
    }

    @Test
    fun `validateInput should return false for empty input`() = runTest {
        val result = viewModel.validateInput("")
        assertFalse(result)
        assertEquals("This field can not be empty.", viewModel.errorMessage.value)
    }

    @Test
    fun `validateInput should return false for non-digit input`() = runTest {
        val result = viewModel.validateInput("abc")
        assertFalse(result)
        assertEquals("Invalid Input", viewModel.errorMessage.value)
    }

    @Test
    fun `validateInput should return true for valid input`() = runTest {
        val result = viewModel.validateInput("123")
        assertTrue(result)
        assertEquals("", viewModel.errorMessage.value)
    }
}
