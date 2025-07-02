package com.iameben.keysmith.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iameben.keysmith.data.model.SettingsItem

@Composable
fun MenuItemRow(item: SettingsItem, onItemClick: () -> Unit) {
    Text(
        text = item.title,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable { onItemClick() }
    )
}