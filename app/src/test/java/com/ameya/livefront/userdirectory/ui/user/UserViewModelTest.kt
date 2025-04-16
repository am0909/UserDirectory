package com.ameya.livefront.userdirectory.ui.user

import com.ameya.livefront.userdirectory.MainDispatcherRule
import com.ameya.livefront.userdirectory.data.repository.TestUserRepository
import com.ameya.livefront.userdirectory.domain.model.Location
import com.ameya.livefront.userdirectory.domain.model.Street
import com.ameya.livefront.userdirectory.domain.model.User
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var repository: TestUserRepository
    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        repository = TestUserRepository()
        viewModel = UserViewModel(repository)
    }

    @Test
    fun loadingState() = runTest {
        repository.setIsLoading(true)
        viewModel.onEvent(UserEvent.OnRefresh)

        assertEquals(true, viewModel.state.isLoading)
        assertEquals(false, viewModel.state.isError)
        assertEquals(true, viewModel.state.userList.isEmpty())
        assertEquals(null, viewModel.state.selectedUserId)
    }

    @Test
    fun errorState() = runTest {
        repository.setIsError(true)
        viewModel.onEvent(UserEvent.OnRefresh)

        assertEquals(false, viewModel.state.isLoading)
        assertEquals(true, viewModel.state.isError)
        assertEquals(true, viewModel.state.userList.isEmpty())
        assertEquals(null, viewModel.state.selectedUserId)
    }

    @Test
    fun getUserList() = runTest {
        repository.addUsers(listOf(getTestUser(1, "John"), getTestUser(2, "Jane")))
        viewModel.onEvent(UserEvent.OnRefresh)

        assertEquals(false, viewModel.state.isLoading)
        assertEquals(false, viewModel.state.isError)
        assertEquals(false, viewModel.state.userList.isEmpty())
        assertEquals(null, viewModel.state.selectedUserId)
    }

    @Test
    fun updateSelectedItem() = runTest {
        viewModel.updateSelectedItem(1)
        assertEquals(null, viewModel.state.selectedUserId)
        assertEquals(false, viewModel.state.isLoading)
        assertEquals(false, viewModel.state.isError)
        assertEquals(true, viewModel.state.userList.isEmpty())

        repository.addUsers(listOf(getTestUser(1, "John")))
        viewModel.onEvent(UserEvent.OnRefresh)
        viewModel.updateSelectedItem(1)
        assertEquals(1L, viewModel.state.selectedUserId)
        assertEquals(false, viewModel.state.isLoading)
        assertEquals(false, viewModel.state.isError)
        assertEquals(1, viewModel.state.userList.size)

        viewModel.updateSelectedItem(null)
        assert(viewModel.state.selectedUserId == null)
        assertEquals(false, viewModel.state.isLoading)
        assertEquals(false, viewModel.state.isError)
        assertEquals(1, viewModel.state.userList.size)
    }

    @Test
    fun getUserListWithFirstSearchQuery() = runTest {
        repository.addUsers(listOf(getTestUser(1, "John"), getTestUser(2, "Jane")))
        viewModel.onEvent(UserEvent.OnSearchQueryChange("Jane"))

        assertEquals(false, viewModel.state.isLoading)
        assertEquals(false, viewModel.state.isError)
        assertEquals(1, viewModel.state.userList.size)
        assertEquals("Jane", viewModel.state.userList[0].first)
        assertEquals(null, viewModel.state.selectedUserId)
    }

    @Test
    fun getUserListWithLastSearchQuery() = runTest {
        repository.addUsers(listOf(getTestUser(1, "John"), getTestUser(2, "Jane", last = "Smith")))
        viewModel.onEvent(UserEvent.OnSearchQueryChange("Smith"))

        assertEquals(false, viewModel.state.isLoading)
        assertEquals(false, viewModel.state.isError)
        assertEquals(1, viewModel.state.userList.size)
        assertEquals("Jane", viewModel.state.userList[0].first)
        assertEquals(null, viewModel.state.selectedUserId)
    }

    @Test
    fun getUserListWithEmptySearchQuery() = runTest {
        repository.addUsers(listOf(getTestUser(1, "John"), getTestUser(2, "Jane")))
        viewModel.onEvent(UserEvent.OnSearchQueryChange(""))

        assertEquals(false, viewModel.state.isLoading)
        assertEquals(false, viewModel.state.isError)
        assertEquals(2, viewModel.state.userList.size)
        assertEquals("John", viewModel.state.userList[0].first)
        assertEquals("Jane", viewModel.state.userList[1].first)
        assertEquals(null, viewModel.state.selectedUserId)
    }

    @Test
    fun getUserListWithWrongSearchQuery() = runTest {
        repository.addUsers(listOf(getTestUser(1, "John")))
        viewModel.onEvent(UserEvent.OnSearchQueryChange("Jane"))

        assertEquals(false, viewModel.state.isLoading)
        assertEquals(false, viewModel.state.isError)
        assertEquals(true, viewModel.state.userList.isEmpty())
        assertEquals(null, viewModel.state.selectedUserId)
    }

    private fun getTestUser(
        id: Long,
        first: String,
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