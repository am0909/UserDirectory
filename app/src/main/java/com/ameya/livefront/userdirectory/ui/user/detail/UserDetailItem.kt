package com.ameya.livefront.userdirectory.ui.user.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Item that displays user information in the Detail pane of the [UserScreen].
 *
 * @param modifier The modifier to be applied to this layout.
 * @param valueModifier The modifier to be applied to the value text.
 * @param label The label to be displayed.
 * @param value The value to be displayed.
 * @param labelTextStyle The text style to be applied to the label.
 * @param valueTextStyle The text style to be applied to the value.
 */
@Composable
fun UserDetailItem(
    modifier: Modifier = Modifier,
    valueModifier: Modifier = Modifier,
    label: String,
    value: String,
    labelTextStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    valueTextStyle: TextStyle = MaterialTheme.typography.bodyLarge
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.then(Modifier.fillMaxWidth())) {
        Text(
            text = label,
            style = labelTextStyle,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = value,
            style = valueTextStyle,
            modifier = valueModifier
        )
    }
}