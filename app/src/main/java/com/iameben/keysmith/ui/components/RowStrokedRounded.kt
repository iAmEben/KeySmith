package com.iameben.keysmith.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun RowStrokedRounded(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
    ) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
          .clip(RoundedCornerShape(8.dp))
          .border(
              width = 1.dp,
              color = MaterialTheme.colorScheme.onBackground,
              shape = RoundedCornerShape(16.dp)
          )
          .background(MaterialTheme.colorScheme.background)
          .padding(16.dp),
        content = content
    )

}