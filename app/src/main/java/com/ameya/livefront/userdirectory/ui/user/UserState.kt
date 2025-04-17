package com.ameya.livefront.userdirectory.ui.user

import com.ameya.livefront.userdirectory.domain.model.User

/**
 * State class for the User screen.
 *
 * @param userList List of users to be displayed.
 * @param isLoading Boolean indicating if data is being loaded.
 * @param isRefreshing Boolean indicating if data is being refreshed.
 * @param searchQuery The query entered in the search field.
 * @param isError Boolean indicating if there is an error.
 * @param selectedUserId ID of the selected user. It will be null when the detail pane is not visible.
 */
data class UserState(
    val userList: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val isError: Boolean = false,
    val selectedUserId: Long? = null
)
