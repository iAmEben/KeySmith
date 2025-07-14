package com.iameben.keysmith.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.iameben.keysmith.R
import com.iameben.keysmith.data.local.entity.PasswordEntity
import com.iameben.keysmith.util.Space

@Composable
fun PasswordItemRow(
    password: PasswordEntity,
    onUpdateTitle: (String) -> Unit,
    onSwipeToDelete: () -> Unit,
    onClick: () -> Unit,
    onIconClick: (SwipeToDismissBoxState) -> Unit,
    ) {
    val backgroundColor = MaterialTheme.colorScheme.onPrimary
    var title by remember { mutableStateOf(password.title) }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.StartToEnd) {
                onSwipeToDelete()
                false
            }else {
                true
            }
        },
        positionalThreshold = { 150f }

    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.EndToStart -> Color.Red
                else -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(start = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onIconClick(dismissState) }
                )
            }
        },
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = false,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ){
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
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
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
                        onClick = { onClick() },
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

    Space()

}