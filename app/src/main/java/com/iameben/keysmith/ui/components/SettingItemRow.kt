package com.iameben.keysmith.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iameben.keysmith.data.model.SettingsItem
import com.iameben.keysmith.util.Space

@Composable
fun SettingItemRow(item: SettingsItem, onItemClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .background(
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(12.dp),

            )
            .padding(16.dp)
    ){
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(8.dp)

        )
    }

    Space()

}