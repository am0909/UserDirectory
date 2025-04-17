package com.ameya.livefront.userdirectory.ui.user.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ameya.livefront.userdirectory.R
import com.ameya.livefront.userdirectory.domain.model.User
import com.ameya.livefront.userdirectory.ui.SearchField
import com.ameya.livefront.userdirectory.ui.user.UserState
import com.ameya.livefront.userdirectory.util.Constants.USER_LIST_LAYOUT_ERROR_TEXT_TEST_TAG
import com.ameya.livefront.userdirectory.util.Constants.USER_LIST_LAYOUT_LOADING_INDICATOR_TEST_TAG
import com.ameya.livefront.userdirectory.util.Constants.USER_LIST_TEST_TAG
import com.ameya.livefront.userdirectory.util.Constants.USER_SEARCH_FIELD_TEST_TAG
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * List pane that displays a list of users in the [UserScreen] [com.ameya.livefront.userdirectory.ui.user.UserScreen].
 *
 * @param modifier The modifier to be applied to this layout.
 * @param state The [UserState] to be displayed.
 * @param onRefreshEvent The callback to be invoked when the user pulls to refresh.
 * @param onSearchQueryChangeEvent The callback to be invoked when the search query changes.
 * @param onItemClick The callback to be invoked when an user item is clicked.
 * @param selectedUserId The ID of the selected user or null if detail pane is not visible.
 */
@Composable
fun UserListLayout(
    modifier: Modifier = Modifier,
    state: UserState,
    onRefreshEvent: () -> Unit,
    onSearchQueryChangeEvent: (String) -> Unit,
    onItemClick: (User) -> Unit,
    selectedUserId: Long?
) {
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isRefreshing
    )

    Column(
        modifier = modifier.then(
            Modifier
                .fillMaxSize()
        )
    ) {
        // TODO: Clear the focus from the search field if user taps outside of it.
        SearchField(
            modifier = Modifier.testTag(USER_SEARCH_FIELD_TEST_TAG),
            onSearchQueryChangeEvent = onSearchQueryChangeEvent
        )

        if (state.isLoading) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.testTag(USER_LIST_LAYOUT_LOADING_INDICATOR_TEST_TAG)
                )
            }
        } else if (state.isError) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.testTag(USER_LIST_LAYOUT_ERROR_TEXT_TEST_TAG),
                    text = stringResource(R.string.error_loading_users),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = onRefreshEvent
        ) {
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .testTag(USER_LIST_TEST_TAG)
            ) {
                itemsIndexed(state.userList) { index, user ->
                    UserListItem(
                        user = user,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onItemClick(it)
                        },
                        isSelected = user.id == selectedUserId
                    )

                    // Add horizontal divider after each item except for the last one
                    if (index < state.userList.size) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

