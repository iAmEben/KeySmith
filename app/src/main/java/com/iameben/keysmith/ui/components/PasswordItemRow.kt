package com.iameben.keysmith.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iameben.keysmith.R
import com.iameben.keysmith.data.local.entity.PasswordEntity
import com.iameben.keysmith.ui.screen.main.MainScreenViewmodel
import com.iameben.keysmith.util.goBack

@Composable
fun PasswordItemRow(
    password: PasswordEntity,
    onUpdateTitle: (String) -> Unit,
    onDelete: () -> Unit,
    mainScreenViewmodel: MainScreenViewmodel = hiltViewModel(),
    context: Context
) {
    val backgroundColor = MaterialTheme.colorScheme.onBackground
    var title by remember { mutableStateOf(password.title) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            BasicTextField(
                value = title,
                onValueChange = { newTitle ->
                    title = newTitle
                    if (newTitle.isNotBlank()) {
                        onUpdateTitle(newTitle)
                    }
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),

            )

            Text(
                text = password.password,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 8.dp)
            )

            Row(
                modifier = Modifier.wrapContentSize().padding(8.dp)
            ) {
                IconButton(
                    onClick = { onDelete },
                    modifier = Modifier
                        .wrapContentSize()
                        .clip(CircleShape)

                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_random),
                        contentDescription = "delete",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(18.dp)
                    )
                }

                IconButton(
                    onClick = { mainScreenViewmodel.copyToClipboard(context, text = password.password) },
                    modifier = Modifier
                        .wrapContentSize()
                        .clip(CircleShape)

                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_smart),
                        contentDescription = "copy",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(18.dp)
                    )
                }
            }
        }
    }
}