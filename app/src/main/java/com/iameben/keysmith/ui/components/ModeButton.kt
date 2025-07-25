package com.iameben.keysmith.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.iameben.keysmith.util.Space

@Composable
fun ModeButton(
    text: String,
    iconRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    index: Int,
    totalCount: Int
) {
    val shape = when {
        selected -> RoundedCornerShape(16.dp)
        index == 0 && totalCount > 1 -> RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp, topEnd = 4.dp, bottomEnd = 4.dp)
        index == totalCount - 1 && totalCount > 1 -> RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp, topEnd = 16.dp, bottomEnd = 16.dp)
        else -> RoundedCornerShape(0.dp)
    }
    Button(
        onClick = onClick,
        colors = if (selected) {
            ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.onBackground)
        }else {
            ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.secondary)
        },
        shape = shape,
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .height(48.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Space(size = 8.dp)
            Text(
                text = text,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}