package com.ameya.livefront.userdirectory.ui.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ameya.livefront.userdirectory.domain.repository.UserRepository
import com.ameya.livefront.userdirectory.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the [UserScreen].
 * @param repository: [UserRepository] - The repository to fetch user data.
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    var state by mutableStateOf(UserState())
        private set

    init {
        // Pass false here since data from remote api will only be needed on the first load and it
        // is handled in the user repository implementation.
        getUserList(requestRemote = false)
    }

    /**
     * Function to handle the user events from the UI.
     * @param event: [UserEvent] - The event to be handled.
     */
    fun onEvent(event: UserEvent) {
        when (event) {
            UserEvent.OnRefresh -> {
                getUserList(requestRemote = true)
            }

            is UserEvent.OnSearchQueryChange -> {
                getUserList(query = event.query, requestRemote = false)
            }
        }
    }

    /**
     * Function to update the selected user id.
     * @param selectedUserId: Long? - The id of the selected user.
     */
    fun updateSelectedItem(selectedUserId: Long?) {
        if (selectedUserId == null || state.userList.isNotEmpty()) {
            state = state.copy(selectedUserId = selectedUserId)
        }
    }

    private fun getUserList(query: String = "", requestRemote: Boolean) {
        viewModelScope.launch {
            repository.getUserList(query = query, requestRemote = requestRemote).collect { result ->
                state = when (result) {
                    is Result.Error -> {
                        state.copy(isLoading = false, isError = true)
                    }

                    is Result.Loading -> {
                        state.copy(isLoading = true, isError = false)
                    }

                    is Result.Success -> {
                        state.copy(isLoading = false, userList = result.data, isError = false)
                    }
                }
            }
        }
    }
}