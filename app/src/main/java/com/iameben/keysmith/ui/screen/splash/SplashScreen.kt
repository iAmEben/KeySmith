package com.iameben.keysmith.ui.screen.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.iameben.keysmith.R
import com.iameben.keysmith.ui.theme.Beige

@Composable
fun SplashScreen(
    isReady: Boolean,
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val alpha = animateFloatAsState(
        targetValue = if (isReady) 0f else 1f,
        animationSpec = tween(durationMillis = 1000),
        label = "fadeOut"
    ).value

    LaunchedEffect(isReady) {
        if (isReady) onNavigateToMain()
    }

    Box (
        modifier = modifier
            .fillMaxSize()
            .background(Beige),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_app_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(120.dp)
                .alpha(alpha)
        )
    }
}