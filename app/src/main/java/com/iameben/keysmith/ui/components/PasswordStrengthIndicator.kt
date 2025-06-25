package com.iameben.keysmith.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iameben.keysmith.ui.theme.DeepRed
import com.iameben.keysmith.ui.theme.Gold
import com.iameben.keysmith.ui.theme.GrayDark
import com.iameben.keysmith.ui.theme.GrayLight
import com.iameben.keysmith.ui.theme.Orange
import com.iameben.keysmith.ui.theme.Red
import com.iameben.keysmith.ui.theme.RusticOrange
import com.iameben.keysmith.ui.theme.YellowBrown

@Composable
fun PasswordStrengthIndicator(
    strength: String,
    isDarkTheme: Boolean
) {
    val (weakColor, okayColor, greatColor) = if (isDarkTheme) {
        Triple(Red, Orange, YellowBrown)
    } else {
        Triple(DeepRed, RusticOrange, Gold)
    }

    val grayColor = if (isDarkTheme) GrayDark else GrayLight

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("weak", "okay", "great").forEach { level ->
            val isActive = strength == level
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = if (isActive) when (level) {
                            "weak" -> weakColor
                            "okay" -> okayColor
                            "great" -> greatColor
                            else -> grayColor
                        }else grayColor,
                        shape = CircleShape

                    )
            )
        }
    }

}