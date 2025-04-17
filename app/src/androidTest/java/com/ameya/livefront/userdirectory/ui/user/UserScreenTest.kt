package com.ameya.livefront.userdirectory.ui.user

import androidx.activity.ComponentActivity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.ForcedSize
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.ameya.livefront.userdirectory.R
import com.ameya.livefront.userdirectory.data.repository.TestUserRepository
import com.ameya.livefront.userdirectory.domain.model.Location
import com.ameya.livefront.userdirectory.domain.model.Street
import com.ameya.livefront.userdirectory.domain.model.User
import com.ameya.livefront.userdirectory.ui.theme.UserDirectoryTheme
import com.ameya.livefront.userdirectory.util.Constants.USER_DETAIL_TEST_TAG
import com.ameya.livefront.userdirectory.util.Constants.USER_LIST_LAYOUT_ERROR_TEXT_TEST_TAG
import com.ameya.livefront.userdirectory.util.Constants.USER_LIST_LAYOUT_LOADING_INDICATOR_TEST_TAG
import com.ameya.livefront.userdirectory.util.Constants.USER_LIST_TEST_TAG
import com.ameya.livefront.userdirectory.util.Constants.USER_SEARCH_FIELD_TEST_TAG
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class UserScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun userListLayoutErrorText() {
        var errorMessage = ""

        composeTestRule.setContent {
            errorMessage = stringResource(id = R.string.error_loading_users)

            UserDirectoryTheme {
                val testUserRepository = TestUserRepository()
                testUserRepository.setIsError(true)
                UserScreen(viewModel = UserViewModel(testUserRepository))
            }
        }

        // Test that the user list is empty
        val users = composeTestRule.onNodeWithTag(USER_LIST_TEST_TAG).onChildren().fetchSemanticsNodes().size
        assertEquals(0, users)
        // Test that the search bar is displayed
        composeTestRule.onNodeWithTag(USER_SEARCH_FIELD_TEST_TAG).assertIsDisplayed()
        // Test that the error message is displayed
        composeTestRule.onNodeWithTag(USER_LIST_LAYOUT_ERROR_TEXT_TEST_TAG)
            .assertIsDisplayed()
            .assertTextEquals(errorMessage)

        // Test that the loading indicator is not displayed
        composeTestRule.onNodeWithTag(USER_LIST_LAYOUT_LOADING_INDICATOR_TEST_TAG)
            .assertDoesNotExist()
    }

    @Test
    fun userListLayoutLoadingIndicator() {
        composeTestRule.setContent {
            UserDirectoryTheme {
                val testUserRepository = TestUserRepository()
                testUserRepository.setIsLoading(true)
                UserScreen(viewModel = UserViewModel(testUserRepository))
            }
        }

        // Test that the user list is empty
        val users = composeTestRule.onNodeWithTag(USER_LIST_TEST_TAG).onChildren().fetchSemanticsNodes().size
        assertEquals(0, users)
        // Test that the search bar is displayed
        composeTestRule.onNodeWithTag(USER_SEARCH_FIELD_TEST_TAG).assertIsDisplayed()
        // Test that the error message is not displayed
        composeTestRule.onNodeWithTag(USER_LIST_LAYOUT_ERROR_TEXT_TEST_TAG).assertDoesNotExist()
        // Test that the loading indicator is displayed
        composeTestRule.onNodeWithTag(USER_LIST_LAYOUT_LOADING_INDICATOR_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun userListLayoutUserList() {
        val userListSize = 5

        composeTestRule.setContent {
            UserDirectoryTheme {
                val testUserRepository = TestUserRepository()
                testUserRepository.addUsers(getTestUserList(userListSize))
                UserScreen(viewModel = UserViewModel(testUserRepository))

            }
        }

        // Test that the search bar is displayed
        composeTestRule.onNodeWithTag(USER_SEARCH_FIELD_TEST_TAG).assertIsDisplayed()
        // Test that the error message is not displayed
        composeTestRule.onNodeWithTag(USER_LIST_LAYOUT_ERROR_TEXT_TEST_TAG).assertDoesNotExist()
        // Test that the loading indicator is displayed
        composeTestRule.onNodeWithTag(USER_LIST_LAYOUT_LOADING_INDICATOR_TEST_TAG).assertDoesNotExist()

        // Test that the user list has userListSize users
        val users = composeTestRule.onNodeWithTag(USER_LIST_TEST_TAG).onChildren().fetchSemanticsNodes().size
        assertEquals(userListSize, users)

        composeTestRule.onNodeWithTag(USER_DETAIL_TEST_TAG).assertDoesNotExist()

        // Test that the user first and last names are displayed
        composeTestRule.onAllNodesWithText("John Doe").assertCountEquals(userListSize)
        
        // Test that the user email is not displayed in list layout
        composeTestRule.onNodeWithText("john.doe@email.com").assertDoesNotExist()
    }

    @Test
    fun userListSearch() {
        composeTestRule.setContent {
            UserDirectoryTheme {
                val userJohn = getTestUser(first = "John", last = "Doe")
                val userJane = getTestUser(first = "Jane", last = "Smith")
                val testUserRepository = TestUserRepository()
                testUserRepository.addUsers(listOf(userJohn, userJane))
                UserScreen(viewModel = UserViewModel(testUserRepository))

            }
        }

        // Test that the search bar is displayed
        composeTestRule.onNodeWithTag(USER_SEARCH_FIELD_TEST_TAG).assertIsDisplayed()
        // Test that the error message is not displayed
        composeTestRule.onNodeWithTag(USER_LIST_LAYOUT_ERROR_TEXT_TEST_TAG).assertDoesNotExist()
        // Test that the loading indicator is displayed
        composeTestRule.onNodeWithTag(USER_LIST_LAYOUT_LOADING_INDICATOR_TEST_TAG).assertDoesNotExist()

        // Test that the user list has userListSize users
        val users = composeTestRule.onNodeWithTag(USER_LIST_TEST_TAG).onChildren().fetchSemanticsNodes().size
        assertEquals(2, users)

        // Search for "John"
        composeTestRule.onNodeWithTag(USER_SEARCH_FIELD_TEST_TAG).performClick()
        composeTestRule.onNodeWithTag(USER_SEARCH_FIELD_TEST_TAG).performTextInput("John")

        // Wait until the search results are updated
        composeTestRule.waitUntil(5000) {
            composeTestRule.onNodeWithTag(USER_LIST_TEST_TAG).onChildren().fetchSemanticsNodes().size == 1
        }

        // Test that the user list has only 1 user
        val userSearched = composeTestRule.onNodeWithTag(USER_LIST_TEST_TAG).onChildren().fetchSemanticsNodes().size
        assertEquals(1, userSearched)

    }

    @Test
    fun userDetailLayout() {
        val userListSize = 5

        composeTestRule.setContent {
            val testUserRepository = TestUserRepository()
            testUserRepository.addUsers(getTestUserList(userListSize))
            UserScreen(viewModel = UserViewModel(testUserRepository))
        }

        // Test that the search bar is displayed
        composeTestRule.onNodeWithTag(USER_SEARCH_FIELD_TEST_TAG).assertExists()
        // Test that the error message is not displayed
        composeTestRule.onNodeWithTag(USER_LIST_LAYOUT_ERROR_TEXT_TEST_TAG).assertDoesNotExist()
        // Test that the loading indicator is displayed
        composeTestRule.onNodeWithTag(USER_LIST_LAYOUT_LOADING_INDICATOR_TEST_TAG).assertDoesNotExist()

        // Test that the user list has userListSize users
        val users = composeTestRule.onNodeWithTag(USER_LIST_TEST_TAG).onChildren().fetchSemanticsNodes().size
        assertEquals(userListSize, users)

        // Test that the user first and last names are displayed
        composeTestRule.onAllNodesWithText("John Doe").assertCountEquals(userListSize)

        // Test that the user email is not displayed in list layout
        composeTestRule.onNodeWithText("john.doe@email.com").assertDoesNotExist()

        // Tap on the first user
        composeTestRule.onAllNodesWithText("John Doe")[0].performClick()

        // Test that the user list is not displayed
        composeTestRule.onNodeWithTag(USER_LIST_TEST_TAG).assertIsNotDisplayed()

        // Test that the user detail screen is displayed
        composeTestRule.onNodeWithTag(USER_DETAIL_TEST_TAG).assertIsDisplayed()

        //Test that the user email is displayed
        composeTestRule.onNodeWithText("john.doe@email.com").assertIsDisplayed()
    }

    @Test
    fun userListDetailLayout() {
        val userListSize = 5

        composeTestRule.setContent {
            // Set the screen size to 1280x800 so as to test both the user list and detail layouts
            DeviceConfigurationOverride(
                DeviceConfigurationOverride.ForcedSize(DpSize(1280.dp, 800.dp))
            ) {
                val testUserRepository = TestUserRepository()
                testUserRepository.addUsers(getTestUserList(userListSize))
                UserScreen(viewModel = UserViewModel(testUserRepository))
            }
        }

        // Test that the search bar is displayed
        composeTestRule.onNodeWithTag(USER_SEARCH_FIELD_TEST_TAG).assertExists()
        // Test that the error message is not displayed
        composeTestRule.onNodeWithTag(USER_LIST_LAYOUT_ERROR_TEXT_TEST_TAG).assertDoesNotExist()
        // Test that the loading indicator is displayed
        composeTestRule.onNodeWithTag(USER_LIST_LAYOUT_LOADING_INDICATOR_TEST_TAG).assertDoesNotExist()

        // Test that the user list has userListSize users
        val users = composeTestRule.onNodeWithTag(USER_LIST_TEST_TAG).onChildren().fetchSemanticsNodes().size
        assertEquals(userListSize, users)

        // Test that the user first and last names are displayed
        composeTestRule.onAllNodesWithText("John Doe").assertCountEquals(userListSize)

        // Test that the user email is not displayed in list layout
        composeTestRule.onNodeWithText("john.doe@email.com").assertDoesNotExist()

        // Tap on the first user
        composeTestRule.onAllNodesWithText("John Doe")[0].performClick()

        // Test that the user detail screen is displayed
        composeTestRule.onNodeWithTag(USER_DETAIL_TEST_TAG).assertIsDisplayed()

        //Test that the user email is displayed
        composeTestRule.onNodeWithText("john.doe@email.com").assertIsDisplayed()

        // Test that the user list is also displayed
        composeTestRule.onNodeWithTag(USER_LIST_TEST_TAG).assertIsDisplayed()

    }

    private fun getTestUserList(size: Int): List<User> {
        val userList = mutableListOf<User>()
        for (i in 0 until size) {
            userList.add(getTestUser(id = i.toLong()))
        }
        return userList
    }

    private fun getTestUser(
        id: Long = 1,
        first: String = "John",
        last: String = "Doe",
    ) = User(
        id = id,
        first = first,
        last = last,
        cell = "1234567890",
        age = 30,
        email = "john.doe@email.com",
        gender = "male",
        location = Location(
            city = "New York",
            country = "USA",
            postcode = "10001",
            state = "NY",
            street = Street(name = "5th Avenue", number = 123),
            timezone = "-05:00"
        ),
        phone = "1234567890",
        large = "large_url",
        medium = "medium_url"
    )
}