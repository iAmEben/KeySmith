package com.iameben.keysmith.ui.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.iameben.keysmith.navigation.Routes
import com.iameben.keysmith.ui.components.MenuItemRow

@Composable
//@Preview(showBackground = true)
fun SettingsScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    settingsViewmodel: SettingsViewmodel = hiltViewModel()
) {
    val itemsState = settingsViewmodel.items.collectAsState()
    val items = itemsState.value

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        items(items) { item ->
            MenuItemRow(
                item = item,
                onItemClick = {
                    when (item.title) {
                        "Your Passwords" -> navController.navigate(Routes.PASSWORDS)
                        "About" -> navController.navigate(Routes.ABOUT)
                    }
                }
            )
        }

    }
}