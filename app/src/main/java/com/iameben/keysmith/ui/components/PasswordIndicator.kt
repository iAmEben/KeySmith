package com.iameben.keysmith.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PasswordIndicator(
    modifier: Modifier = Modifier,
    dotSize: Dp = 8.dp,
    dotColor1: Color = Color.Gray,
    dotColor2: Color = Color.Gray,
    dotColor3: Color = Color.Gray
) {


    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Dot(
            size = dotSize,
            color = dotColor1
        )

        Dot(
            size = dotSize,
            color = dotColor2
        )

        Dot(
            size = dotSize,
            color = dotColor3
        )
    }
}