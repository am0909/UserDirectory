package com.ameya.livefront.userdirectory.ui.user

sealed interface UserEvent {
    data object OnRefresh: UserEvent
    data class OnSearchQueryChange(val query: String): UserEvent
}