package com.iameben.keysmith.ui.components

import android.R.attr.action
import android.R.attr.content
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomSnackBarHost(
    hostState: SnackbarHostState
) {
    SnackbarHost(hostState = hostState) { snackBarData ->
        Snackbar(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.secondary ),
            action = snackBarData.visuals.actionLabel?.let {
                { snackBarData.performAction() }
            }
        ){
            Text(
               text = snackBarData.visuals.message,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }


    }
}