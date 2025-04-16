package com.ameya.livefront.userdirectory.ui.user

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ameya.livefront.userdirectory.domain.model.User
import com.ameya.livefront.userdirectory.ui.user.detail.UserDetailLayout
import com.ameya.livefront.userdirectory.ui.user.list.UserListLayout
import kotlinx.coroutines.launch

/**
 * Composable function that displays the user list and detail information in a two-pane layout.
 *
 * @param modifier The modifier to be applied to this layout.
 * @param viewModel The [UserViewModel] to be used to fetch the list of users.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel()
) {
    val navigator = rememberListDetailPaneScaffoldNavigator()
    val scope = rememberCoroutineScope()

    // The callback to be invoked when the user swipes to refresh
    val onRefreshEvent = {
        viewModel.onEvent(UserEvent.OnRefresh)
    }

    // The callback to be invoked when the search query changes
    val onSearchQueryChangeEvent: (String) -> Unit = { query ->
        viewModel.onEvent(UserEvent.OnSearchQueryChange(query))
    }

    // The callback to be invoked when the selected item changes
    val onUpdateSelectedItem: (Long?) -> Unit = { selectedUserId ->
        viewModel.updateSelectedItem(selectedUserId)
    }

    // Check if the detail pane is visible.
    val isDetailPaneVisible =
        navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded

    // If detail pane is not visible, update the selected item to null so that nothing
    // highlighted in list pane
    if (!isDetailPaneVisible) {
        onUpdateSelectedItem(null)
    }

    // When a user is clicked in the list layout, pass it as the content key to the navigator so that
    // it can be used to display its detail page
    val onItemClick: (User) -> Unit = { user ->
        onUpdateSelectedItem(user.id)

        scope.launch {
            navigator.navigateTo(
                pane = ListDetailPaneScaffoldRole.Detail,
                contentKey = user
            )
        }
    }

    // If the detail pane is visible, the selected user id will be the id of the user that was
    // clicked in the list layout else it will be null.
    val selectedUserId = if (isDetailPaneVisible) {
        viewModel.state.selectedUserId
    } else {
        null
    }

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
        listPane = {
            UserListLayout(
                state = viewModel.state,
                onRefreshEvent = onRefreshEvent,
                onSearchQueryChangeEvent = onSearchQueryChangeEvent,
                onItemClick = { user ->
                    onItemClick(user)
                },
                selectedUserId = selectedUserId,
            )
        },
        detailPane = {
            // Fetch the user that was passed as the content key when it was clicked in the
            // list layout and navigate to its detail layout
            navigator.currentDestination?.contentKey?.let { user ->
                UserDetailLayout(user = user as User)
            }
        }
    )
}