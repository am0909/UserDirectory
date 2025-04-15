package com.ameya.livefront.userdirectory.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.ameya.livefront.userdirectory.domain.model.User

@Composable
fun UserListItem(
    user: User,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(
                text = user.first + " " + user.last
            )
        },
        supportingContent = {
            Text(
                text = user.location.country
            )
        },
        leadingContent = {
            AsyncImage(
                model = user.medium,
                contentDescription = null
            )
        },
        trailingContent = {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = user.age.toString())

                Text(
                    text = user.gender
                )
            }
        }
    )
}