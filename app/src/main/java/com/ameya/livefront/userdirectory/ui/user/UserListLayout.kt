package com.ameya.livefront.userdirectory.ui.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ameya.livefront.userdirectory.R
import com.ameya.livefront.userdirectory.domain.model.User
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * List pane that displays a list of users in the [UserListDetailPaneScaffold].
 *
 * @param modifier The modifier to be applied to this layout.
 * @param viewModel The [UserViewModel] instance to be used.
 * @param navigator The [ThreePaneScaffoldNavigator] instance to be used for navigation.
 * @param onItemClick The callback to be invoked when an user item is clicked.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun UserListLayout(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel(),
    navigator: ThreePaneScaffoldNavigator<Any>,
    onItemClick: (User) -> Unit
) {
    val state = viewModel.state
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isRefreshing
    )

    Column(
        modifier = modifier.then(
            Modifier
                .fillMaxSize()
        )
    ) {
        UserSearchField(viewModel)

        if (state.isLoading) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.isError) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.error_loading_users),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }

            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    viewModel.onEvent(UserEvent.OnRefresh)
                }
            ) {
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize()
                ) {
                    // Check if the detail pane is visible.
                    val isDetailPaneVisible =
                        navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded

                    // If detail pane is not visible, update the selected item to null so that nothing
                    // highlighted in list pane
                    if (!isDetailPaneVisible) {
                        viewModel.updateSelectedItem(null)
                    }

                    itemsIndexed(state.userList) { index, user ->
                        UserListItem(
                            user = user,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                onItemClick(it)
                                viewModel.updateSelectedItem(it.id)
                            },
                            isSelected = isDetailPaneVisible && (user.id == viewModel.state.selectedUserId)
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

@Composable
private fun UserSearchField(
    viewModel: UserViewModel
) {
    var value by rememberSaveable { mutableStateOf("") }

    val onValueChange: (String) -> Unit = {
        value = it
        viewModel.onEvent(UserEvent.OnSearchQueryChange(it))
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(stringResource(R.string.search_placeholder))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_leading_icon_content_desc)
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(
                    onClick = { onValueChange("") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.search_trailing_icon_content_desc)
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(24.dp)
    )
}
