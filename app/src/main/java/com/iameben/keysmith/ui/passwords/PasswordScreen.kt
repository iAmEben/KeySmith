package com.iameben.keysmith.ui.passwords

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.iameben.keysmith.R
import com.iameben.keysmith.data.local.entity.PasswordEntity
import com.iameben.keysmith.navigation.Routes
import com.iameben.keysmith.ui.components.CustomSnackBarHost
import com.iameben.keysmith.ui.components.PasswordItemRow
import com.iameben.keysmith.ui.components.SettingItemRow
import com.iameben.keysmith.ui.screen.main.MainScreenViewmodel
import com.iameben.keysmith.util.Space
import com.iameben.keysmith.util.goBack
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun PasswordScreen(
    navController: NavHostController,
    viewmodel: PasswordViewmodel = hiltViewModel(),
    mainScreenViewmodel: MainScreenViewmodel = hiltViewModel()
) {

    val passwords = viewmodel.passwords.collectAsState().value
    var passwordToDelete by remember { mutableStateOf<PasswordEntity?>(null)}
    var dismissStateToReset by remember { mutableStateOf<SwipeToDismissBoxState?>(null) }
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(snackBarHostState) {
        if (snackBarHostState != mainScreenViewmodel.snackBarHostState.value){
            mainScreenViewmodel.setSnackBarHotState(snackBarHostState)
        }

    }

    Scaffold(
        snackbarHost = { CustomSnackBarHost(hostState = snackBarHostState) },
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = { goBack(navController) },
                    modifier = Modifier
                        .wrapContentSize()
                        .clip(CircleShape)

                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = "back",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(18.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Your Passwords",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }


            }


            Space(size = 32.dp)

            LazyColumn(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .wrapContentSize()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (passwords.isEmpty()) {
                    item {
                        Text(
                            text = "No passwords Saved",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(passwords, key = { it.id }) { password ->
                        PasswordItemRow(
                            password = password,
                            onUpdateTitle = { newTitle ->
                                viewmodel.updatePasswordTitle(password, newTitle)
                            },
                            onSwipeToDelete = {
                                passwordToDelete = password
                                dismissStateToReset = null
                            },
                            onClick = {
                                mainScreenViewmodel.copyToClipboard(
                                    context,
                                    text = password.password
                                )
                            },
                            onIconClick = { state ->
                                passwordToDelete = password
                                dismissStateToReset = state
                                scope.launch { state.reset() }

                            }
                        )
                    }
                }

            }
        }
    }

}