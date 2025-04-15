package com.ameya.livefront.userdirectory.ui

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ameya.livefront.userdirectory.ui.user.UserListLayout

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun UserListDetailPaneScaffold(
    modifier: Modifier = Modifier
) {
    val navigator = rememberListDetailPaneScaffoldNavigator()

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
        listPane = {
            AnimatedPane {
                UserListLayout()
            }
        },
        detailPane = {

        }
    )
}