package com.akumar.randomstringgenerator

import com.akumar.randomstringgenerator.data.database.IRandomStringsDatabaseRepository
import com.akumar.randomstringgenerator.data.model.RandomStringItem
import com.akumar.randomstringgenerator.data.repository.IRandomStringRepository
import com.akumar.randomstringgenerator.ui.screens.randomStringScreen.RandomStringFetchResult
import com.akumar.randomstringgenerator.ui.screens.randomStringScreen.RandomStringViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalCoroutinesApi
class RandomStringViewModelTest {
    private lateinit var viewModel: RandomStringViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val randomStringRepository: IRandomStringRepository = mockk()
    private val randomStringsDatabaseRepository: IRandomStringsDatabaseRepository =
        mockk(relaxed = true)

    private val testItem = RandomStringItem(
        id = 1,
        value = "ABCDEF",
        length = 6,
        created = "26 May, 2025 10:00:00"
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { randomStringsDatabaseRepository.randomStringsList } returns flowOf(listOf(testItem))

        viewModel = RandomStringViewModel(
            randomStringRepository,
            randomStringsDatabaseRepository,
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchRandomString should emit Loading then Success and insert item`() = runTest {
        // Arrange
        val result = RandomStringFetchResult.Success(testItem)

        coEvery { randomStringRepository.getRandomString(6) } returns result
        coEvery { randomStringsDatabaseRepository.insert(testItem) } just Runs

        // When
        viewModel.fetchRandomString(6)

        // Simulate coroutine execution
        advanceUntilIdle()

        // Then
        val resultState = viewModel.result.value
        assertTrue(resultState is RandomStringFetchResult.Success)
        assertEquals(testItem, (resultState as RandomStringFetchResult.Success).randomStringItem)

        assertEquals("", viewModel.inputValue.value)
        coVerify { randomStringsDatabaseRepository.insert(testItem) }
    }

    @Test
    fun `fetchRandomString should emit Loading then Error on failure`() = runTest {
        // Arrange
        val errorResult = RandomStringFetchResult.Error("Some error")
        coEvery { randomStringRepository.getRandomString(any()) } returns errorResult

        // Act
        viewModel.fetchRandomString(6)
        advanceUntilIdle()

        // Assert
        assert(viewModel.result.value is RandomStringFetchResult.Error)
    }

    @Test
    fun `deleteAllStrings should call deleteAllRandomStrings`() = runTest {
        coEvery { randomStringsDatabaseRepository.deleteAllRandomStrings() } just Runs

        viewModel.deleteAllStrings()
        advanceUntilIdle()

        coVerify { randomStringsDatabaseRepository.deleteAllRandomStrings() }
    }

    @Test
    fun `deleteString should call delete with correct item`() = runTest {
        coEvery { randomStringsDatabaseRepository.delete(testItem) } just Runs

        viewModel.deleteString(testItem)
        advanceUntilIdle()

        coVerify { randomStringsDatabaseRepository.delete(testItem) }
    }

    @Test
    fun `validateInput should return false and set error on empty input`() {
        val isValid = viewModel.validateInput("")
        assertFalse(isValid)
        assertEquals("This field can not be empty.", viewModel.errorMessage.value)
    }

    @Test
    fun `validateInput should return false and set error on non-digit input`() {
        val isValid = viewModel.validateInput("12ab")
        assertFalse(isValid)
        assertEquals("Invalid Input", viewModel.errorMessage.value)
    }

    @Test
    fun `validateInput should return true and clear error on valid input`() {
        val isValid = viewModel.validateInput("123")
        assertTrue(isValid)
        assertEquals("", viewModel.errorMessage.value)
    }

    @Test
    fun `setInputValue should update inputValue`() {
        viewModel.setInputValue("789")
        assertEquals("789", viewModel.inputValue.value)
    }
}
