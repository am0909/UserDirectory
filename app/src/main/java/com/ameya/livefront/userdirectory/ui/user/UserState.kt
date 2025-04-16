package com.ameya.livefront.userdirectory.ui.user

import com.ameya.livefront.userdirectory.domain.model.User

/**
 * State class for User screen.
 *
 * @param userList List of users to be displayed.
 * @param isLoading Boolean indicating if data is being loaded.
 * @param isRefreshing Boolean indicating if data is being refreshed.
 * @param searchQuery The search query.
 * @param isError Boolean indicating if there was an error.
 * @param selectedUserId ID of the selected user.
 */
data class UserState(
    val userList: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val isError: Boolean = false,
    val selectedUserId: Long? = null
)
