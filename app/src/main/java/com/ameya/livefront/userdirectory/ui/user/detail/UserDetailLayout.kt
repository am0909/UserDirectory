package com.ameya.livefront.userdirectory.ui.user.detail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.ameya.livefront.userdirectory.R
import com.ameya.livefront.userdirectory.domain.model.User
import com.ameya.livefront.userdirectory.util.Constants.USER_DETAIL_TEST_TAG

/**
 * Detail pane that displays user information in the [UserScreen] [com.ameya.livefront.userdirectory.ui.user.UserScreen].
 *
 * @param modifier The modifier to be applied to this layout.
 * @param user The user to display.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserDetailLayout(
    modifier: Modifier = Modifier,
    user: User
) {
    val context = LocalContext.current

    LazyColumn(
        state = LazyListState(),
        modifier = modifier.then(
            Modifier
                .fillMaxSize()
                .padding(12.dp)
                .testTag(USER_DETAIL_TEST_TAG)
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val imageUrl = user.large
        if (imageUrl.isNotBlank()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = stringResource(
                            R.string.image_content_description,
                            "${user.first} ${user.last}"
                        ),
                        error = painterResource(R.drawable.user_placeholder),
                        placeholder = painterResource(R.drawable.user_placeholder),
                        modifier = Modifier
                            .size(256.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        stickyHeader {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${user.first} ${user.last}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            val phone = user.phone
            UserDetailItem(
                label = stringResource(R.string.phone),
                value = phone,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = "tel:$phone".toUri()
                    }
                    try {
                        context.startActivity(intent)
                    } catch (ex: ActivityNotFoundException) {
                        Toast.makeText(context, R.string.no_app_to_handle_action, Toast.LENGTH_SHORT).show()
                    }
                },
                valueTextStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    textDecoration = TextDecoration.Underline
                )
            )
        }

        item {
            val cell = user.cell
            UserDetailItem(
                label = stringResource(R.string.cell_phone),
                value = cell,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = "tel:$cell".toUri()
                    }
                    try {
                        context.startActivity(intent)
                    } catch (ex: ActivityNotFoundException) {
                        Toast.makeText(context, R.string.no_app_to_handle_action, Toast.LENGTH_SHORT).show()
                    }
                },
                valueTextStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    textDecoration = TextDecoration.Underline
                )
            )
        }

        item {
            val email = user.email
            UserDetailItem(
                label = stringResource(R.string.email),
                value = email,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = "mailto:$email".toUri()
                    }
                    try {
                        context.startActivity(intent)
                    } catch (ex: ActivityNotFoundException) {
                        Toast.makeText(context, R.string.no_app_to_handle_action, Toast.LENGTH_SHORT).show()
                    }
                },
                valueTextStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    textDecoration = TextDecoration.Underline
                )
            )
        }

        item {
            val location = user.location
            val street = location.street
            UserDetailItem(
                label = stringResource(R.string.address),
                value = "${street.number} ${street.name}\n${location.city}\n${location.state} ${location.postcode}\n${location.country}"
            )
        }

        item {
            UserDetailItem(
                label = stringResource(R.string.timezone),
                value = user.location.timezone
            )
        }

        item {
            UserDetailItem(
                label = stringResource(R.string.age),
                value = user.age.toString()
            )
        }

        item {
            val currentLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.resources.configuration.locales.get(0)
            } else {
                context.resources.configuration.locale
            }
            UserDetailItem(
                label = stringResource(R.string.gender),
                value = user.gender.replaceFirstChar { if (it.isLowerCase()) it.titlecase(currentLocale) else it.toString() }
            )
        }
    }
}
