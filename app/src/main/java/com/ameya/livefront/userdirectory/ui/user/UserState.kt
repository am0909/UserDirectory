package com.ameya.livefront.userdirectory.ui.user

import com.ameya.livefront.userdirectory.domain.model.User

data class UserState(
    val userList: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
)
