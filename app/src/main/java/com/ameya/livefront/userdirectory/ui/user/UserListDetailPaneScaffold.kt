package com.ameya.livefront.userdirectory.ui.user

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.ameya.livefront.userdirectory.domain.model.User
import kotlinx.coroutines.launch

/**
 * A [NavigableListDetailPaneScaffold] that displays a list of users in the list pane and the details of a user in the detail pane.
 * The detail pane is displayed when a user is clicked in the list pane.
 *
 * @param modifier The modifier to be applied to this layout.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun UserListDetailPaneScaffold(
    modifier: Modifier = Modifier
) {
    val navigator = rememberListDetailPaneScaffoldNavigator()
    val scope = rememberCoroutineScope()

    // When a user is clicked in the list layout, pass it as the content key to the navigator so that
    // it can be used to display its detail page
    val onItemClick: (User) -> Unit = { user ->
        scope.launch {
            navigator.navigateTo(
                pane = ListDetailPaneScaffoldRole.Detail,
                contentKey = user
            )
        }
    }

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
        listPane = {
            UserListLayout(
                onItemClick = { user ->
                    onItemClick(user)
                },
                navigator = navigator
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