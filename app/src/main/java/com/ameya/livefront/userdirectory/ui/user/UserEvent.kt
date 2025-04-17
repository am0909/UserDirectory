package com.ameya.livefront.userdirectory.ui.user

/**
 * Interface to handle various user events.
 */
sealed interface UserEvent {
    /**
     * Event triggered when the user swipes to refresh.
     */
    data object OnRefresh: UserEvent
    /**
     * Event triggered when the user types a query in the search field.
     */
    data class OnSearchQueryChange(val query: String): UserEvent
}