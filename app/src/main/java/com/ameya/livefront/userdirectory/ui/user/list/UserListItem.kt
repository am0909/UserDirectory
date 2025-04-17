package com.ameya.livefront.userdirectory.ui.user.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ameya.livefront.userdirectory.R
import com.ameya.livefront.userdirectory.domain.model.User

/**
 * List item that displays user information in the List pane of the [UserScreen] [com.ameya.livefront.userdirectory.ui.user.UserScreen].
 *
 * @param user The user to display.
 * @param modifier The modifier to be applied to this layout.
 * @param onClick The callback invoked when the item is clicked.
 * @param isSelected Whether the item is selected or not.
 */
@Composable
fun UserListItem(
    user: User,
    modifier: Modifier = Modifier,
    onClick: (User) -> Unit,
    isSelected: Boolean
) {
    ListItem(
        modifier = modifier.clickable(
            onClick = { onClick(user) },
            indication = null,
            interactionSource = null
        ),
        headlineContent = {
            Text(
                text = "${user.first} ${user.last}"
            )
        },
        leadingContent = {
            AsyncImage(
                model = user.large,
                contentDescription = null,
                error = painterResource(R.drawable.user_placeholder),
                placeholder = painterResource(R.drawable.user_placeholder),
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
            )

        },
        colors = ListItemDefaults.colors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.background
            }
        )
    )
}