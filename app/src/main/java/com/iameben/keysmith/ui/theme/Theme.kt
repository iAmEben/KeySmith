package com.iameben.keysmith.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

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
    onSurfaceVariant = WarmSandTan.copy(alpha = 0.37f)
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
    onBackground = DarkGray,
    onSurface = DeepCocoaBrown,
    onSurfaceVariant = DeepCocoaBrown.copy(alpha = 0.37f)

)

private val CustomDarkTextStyle = TextStyle(
    color = WarmSandTan
)

private val CustomTextStyle = TextStyle(
    color = DarkGray
)


@Composable
fun KeySmithTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable (() -> Unit)
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
            if (darkTheme) DarkColorScheme else LightColorScheme
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val textStyle = if (darkTheme) CustomDarkTextStyle else CustomTextStyle

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = {
            ProvideTextStyle(textStyle, content = content)
        }
    )
}