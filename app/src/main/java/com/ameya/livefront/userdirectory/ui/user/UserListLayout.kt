package com.ameya.livefront.userdirectory.ui.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ameya.livefront.userdirectory.R
import com.ameya.livefront.userdirectory.domain.model.User
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun UserListLayout(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.state.isRefreshing
    )

    Column(
        modifier = modifier.then(
            Modifier
                .fillMaxSize()
        )
    ) {
        UserSearchField(viewModel)

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
                itemsIndexed(state.userList) { index, item ->
                    UserListItem(
                        user = item,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (index < state.userList.size) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun UserSearchField(viewModel: UserViewModel) {
    var value by rememberSaveable { mutableStateOf("") }

    val onValueChange: (String) -> Unit = {
        value = it
        viewModel.onEvent(UserEvent.OnSearchQueryChange(it))
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
        singleLine = true
    )
}
