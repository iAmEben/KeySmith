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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordScreen(
    navController: NavHostController,
    viewmodel: PasswordViewmodel = hiltViewModel(),
    mainScreenViewmodel: MainScreenViewmodel = hiltViewModel()
) {

    var showDialog by remember { mutableStateOf(false) }
    val passwords = viewmodel.passwords.collectAsState().value
    var passwordToDelete by remember { mutableStateOf<PasswordEntity?>(null)}
    var dismissStateToReset by remember { mutableStateOf<SwipeToDismissBoxState?>(null) }
    var currentlySwipedState by remember { mutableStateOf<SwipeToDismissBoxState?>(null) }
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(snackBarHostState) {
        if (snackBarHostState != mainScreenViewmodel.snackBarHostState.value){
            mainScreenViewmodel.setSnackBarHotState(snackBarHostState)
        }

    }

    if (showDialog && passwordToDelete != null) {

        BasicAlertDialog(
            onDismissRequest = {
                showDialog = false
                passwordToDelete = null
                scope.launch {
                    currentlySwipedState?.reset()
                    currentlySwipedState = null
                }
            },
            modifier = Modifier
                .background(
                    color = AlertDialogDefaults.containerColor,
                    shape = AlertDialogDefaults.shape
                )
        ) {
            Surface(
                color = AlertDialogDefaults.containerColor,
                tonalElevation = AlertDialogDefaults.TonalElevation,
                shape = AlertDialogDefaults.shape
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Confirm Deletion",
                        style = MaterialTheme.typography.headlineSmall,
                        color = AlertDialogDefaults.titleContentColor
                    )
                    Text(
                        text = "Are you sure you want to delete '${passwordToDelete!!.title}'? This cannot be undone",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AlertDialogDefaults.textContentColor
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                showDialog = false
                                passwordToDelete = null
                                scope.launch {
                                    currentlySwipedState?.reset()
                                    currentlySwipedState = null
                                }
                            }
                        ) { Text("Cancel") }

                        TextButton(
                            onClick = {
                                viewmodel.deletePassword(passwordToDelete!!)
                                showDialog = false
                                passwordToDelete = null
                                scope.launch {
                                    currentlySwipedState?.reset()
                                    currentlySwipedState = null
                                }
                            }
                        ) { Text("Delete")}
                    }

                }
            }
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
                                showDialog = true
                            },
                            onClick = {
                                mainScreenViewmodel.copyToClipboard(
                                    context,
                                    text = password.password
                                )
                            },
                            onIconClick = { state ->
                                scope.launch {
                                    currentlySwipedState?.reset()
                                    currentlySwipedState = null
                                }
                                passwordToDelete = password
                                showDialog = true
                                scope.launch { state.reset() }

                            }
                        )
                    }
                }

            }
        }
    }

}