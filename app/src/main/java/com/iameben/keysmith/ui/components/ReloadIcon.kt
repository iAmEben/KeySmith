package com.iameben.keysmith.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.iameben.keysmith.R
import kotlinx.coroutines.delay

@Composable
fun ReloadIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    var rotated by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (rotated) 90f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    IconButton(
        onClick = {
            rotated = true
            onClick()
        },
        modifier = modifier
            .size(24.dp),
        interactionSource = remember { MutableInteractionSource() }

    ) {
        Icon(
            painter = painterResource(R.drawable.ic_reload),
            contentDescription = "Reload",
            modifier = Modifier.graphicsLayer(rotationZ = rotationAngle),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }

    LaunchedEffect(rotated) {
        if (rotated){
            delay(200)
            rotated = false
        }
    }
}