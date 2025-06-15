package com.iameben.keysmith.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkMocha,
    secondary = GoldenOrange,
    tertiary = BurntOrange,
    background = DeepCharcoalBlack,
    surface = DeepAshGray,
    onPrimary = PureWhite,
    onSecondary = WarmSandTan,
    onTertiary = SoftAmber,
    onBackground = WarmSandTan,
    onSurface = WarmSandTan,
)

private val LightColorScheme = lightColorScheme(
    primary = DarkBrown,
    secondary = GoldenBrown,
    tertiary = WarmBrown,
    background = Beige,
    surface = LightApricotBeige,
    onPrimary = PureWhite,
    onSecondary = DeepBrown,
    onTertiary = LightApricotBeige,
    onBackground = DeepCocoaBrown,
    onSurface = DeepCocoaBrown,

)

@Composable
fun KeySmithTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}