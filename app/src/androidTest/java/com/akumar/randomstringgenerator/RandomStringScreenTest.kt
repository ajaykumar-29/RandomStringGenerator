package com.akumar.randomstringgenerator

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RandomStringTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private val validTestString = "9785"
    private val invalidTestString = "invalid"

    @Test
    fun should_DisplayInitialUIState_WhenLaunched() {
        rule.onNodeWithText(rule.activity.getString(R.string.enter_string_size)).assertExists()
        rule.onNodeWithText(rule.activity.getString(R.string.generate_string)).assertExists()
        rule.onNodeWithText(rule.activity.getString(R.string.no_data)).assertExists()
        rule.onNodeWithText(rule.activity.getString(R.string.delete_all)).assertDoesNotExist()
    }

    @Test
    fun should_DisplayValidInput_WhenEnteredInInputField() {
        rule.onNodeWithText(rule.activity.getString(R.string.enter_string_size))
            .performTextInput(validTestString)
        rule.onNodeWithText(validTestString).assertExists()
    }

    @Test
    fun should_ShowErrorMessage_WhenInvalidInputEntered() {
        rule.onNodeWithText(rule.activity.getString(R.string.enter_string_size))
            .performTextInput(invalidTestString)
        rule.onNodeWithText(rule.activity.getString(R.string.invalid_input)).assertExists()
    }

    @Test
    fun should_FetchRandomString_WhenValidInputAndFetchButtonClicked() {
        rule.onNodeWithText(rule.activity.getString(R.string.enter_string_size))
            .performTextInput(validTestString)
        rule.onNodeWithText(rule.activity.getString(R.string.generate_string)).performClick()
    }

    @Test
    fun should_ShowFieldEmptyError_WhenGenerateClickedWithoutInput() {
        rule.onNodeWithText(rule.activity.getString(R.string.generate_string))
            .performClick()
        rule.onNodeWithText(rule.activity.getString(R.string.field_can_not_be_empty))
            .assertExists()
    }
}
